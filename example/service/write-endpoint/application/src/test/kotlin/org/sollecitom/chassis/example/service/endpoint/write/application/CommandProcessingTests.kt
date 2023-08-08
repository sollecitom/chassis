package org.sollecitom.chassis.example.service.endpoint.write.application

import assertk.assertThat
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.traits.Identifiable
import org.sollecitom.chassis.core.domain.traits.Timestamped
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.core.domain.versioning.Versioned
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreUtils
import org.sollecitom.chassis.example.service.endpoint.write.application.RegisterUserCommand.V1.Result.Accepted
import org.sollecitom.chassis.example.service.endpoint.write.application.RegisterUserCommand.V1.Result.Rejected.EmailAddressAlreadyInUse

@TestInstance(PER_CLASS)
private class CommandProcessingTests : WithCoreUtils by WithCoreUtils.testProvider {

    @Test
    fun `registering a new user`() = runTest {

        val application = newApplication()
        val emailAddress = "someone@somedomain.com".let(::EmailAddress)
        val registerUser = registerUser(emailAddress = emailAddress).asApplicationCommand()

        val result = application(registerUser)

        assertThat(result).isInstanceOf<Accepted>()
    }

    @Test
    fun `a user attempting to register with an email address already in use`() = runTest {

        val application = newApplication()
        val emailAddress = "someone@somedomain.com".let(::EmailAddress)
        val registerUserAFirstTime = registerUser(emailAddress = emailAddress).asApplicationCommand()
        application(registerUserAFirstTime).let { check(it is Accepted) }

        val registerUserASecondTime = registerUser(emailAddress = emailAddress).asApplicationCommand()
        val result = application(registerUserASecondTime)

        assertThat(result).isInstanceOf<EmailAddressAlreadyInUse>()
    }

    private fun registerUser(emailAddress: EmailAddress): RegisterUser.V1 {

        return RegisterUser.V1(emailAddress = emailAddress)
    }

    private fun newApplication(events: EventStore.Mutable = InMemoryEventStore(), clock: Clock = this.clock, userRepository: UserRepository = InMemoryUserRepository(events = events, newId = newId.ulid, clock = clock)): Application = DispatchingApplication(userRepository::withEmailAddress)
}

class DispatchingApplication(private val userWithEmailAddress: suspend (EmailAddress) -> User) : Application {

    @Suppress("UNCHECKED_CAST")
    override suspend fun <RESULT> invoke(command: ApplicationCommand<RESULT>) = when (command) {
        is RegisterUserCommand.V1 -> process(command) as RESULT
    }

    private suspend fun process(command: RegisterUserCommand.V1): RegisterUserCommand.V1.Result {

        val user = userWithEmailAddress(command.emailAddress)
        return try {
            user.submitRegistrationRequest()
            Accepted
        } catch (error: UserAlreadyRegisteredException) {
            EmailAddressAlreadyInUse(user.id)
        }
    }
}

interface UserRepository {

    suspend fun withEmailAddress(emailAddress: EmailAddress): User
}

interface EventStore {

    val stream: Flow<Event>

    fun history(): Flow<Event>

    fun forEntity(entityId: SortableTimestampedUniqueIdentifier<*>): EntityEventStore

    interface Mutable : EventStore {

        suspend fun publish(event: Event)

        override fun forEntity(entityId: SortableTimestampedUniqueIdentifier<*>): EntityEventStore.Mutable
    }
}

interface EntityEventStore {

    val entityId: SortableTimestampedUniqueIdentifier<*>

    val stream: Flow<EntityEvent>

    fun history(): Flow<EntityEvent>

    interface Mutable : EntityEventStore {

        suspend fun publish(event: EntityEvent)
    }
}

class InMemoryEventStore : EventStore.Mutable {

    private val _stream = MutableSharedFlow<Event>()
    private val history = mutableListOf<Event>()
    private val mutex = Mutex()

    override suspend fun publish(event: Event) = mutex.withLock {
        history += event
        _stream.emit(event)
    }

    override fun history() = history.asFlow()

    override val stream: Flow<Event> get() = _stream

    override fun forEntity(entityId: SortableTimestampedUniqueIdentifier<*>): EntityEventStore.Mutable = EntityEventStoreView(entityId)

    private inner class EntityEventStoreView(override val entityId: SortableTimestampedUniqueIdentifier<*>) : EntityEventStore.Mutable {

        override suspend fun publish(event: EntityEvent) {

            require(event.isForEntity(entityId)) { "Cannot publish event for entityId '${event.entityId.stringValue}'. Expected entityId is '${entityId.stringValue}'" }
            this@InMemoryEventStore.publish(event)
        }

        override fun history() = this@InMemoryEventStore.history().forEntity(entityId)

        override val stream = this@InMemoryEventStore.stream.forEntity(entityId)

        private fun Flow<Event>.forEntity(entityId: SortableTimestampedUniqueIdentifier<*>): Flow<EntityEvent> = filterIsInstance<EntityEvent>().filter { it.entityId == entityId }
        private fun EntityEvent.isForEntity(entityId: SortableTimestampedUniqueIdentifier<*>): Boolean = this.entityId == entityId
    }
}

class InMemoryUserRepository(private val events: EventStore.Mutable, private val newId: SortableTimestampedUniqueIdentifierFactory<*>, private val clock: Clock) : UserRepository {

    private val userByEmail = mutableMapOf<EmailAddress, User>()

    override suspend fun withEmailAddress(emailAddress: EmailAddress) = userByEmail.computeIfAbsent(emailAddress, ::createNewUser)

    private fun createNewUser(emailAddress: EmailAddress): User {

        val userId = newId()
        return EventSourcedUser(_events = events.forEntity(userId), id = userId, emailAddress = emailAddress, newId = newId, clock = clock)
    }

    private class EventSourcedUser(override val id: SortableTimestampedUniqueIdentifier<*>, private val emailAddress: EmailAddress, private val _events: EntityEventStore.Mutable, private val newId: SortableTimestampedUniqueIdentifierFactory<*>, private val clock: Clock) : User {

        private val history get() = _events.history()
        override val events: EntityEventStore get() = _events
        private val mutex = Mutex()

        init {
            require(events.entityId == id) { "The entity ID for the entity-specific event store '${events.entityId}' doesn't match the entity ID of the user '$id'" }
        }

        override suspend fun submitRegistrationRequest() = mutex.withLock {

            ensureRegistrationWasNotAlreadySubmitted()
            val event = registrationRequestWasSubmitted()
            publish(event)
        }

        private fun registrationRequestWasSubmitted(): RegistrationRequestWasSubmitted.V1 {

            val now = clock.now()
            return RegistrationRequestWasSubmitted.V1(emailAddress = emailAddress, userId = id, id = newId(now), timestamp = now)
        }

        private suspend fun ensureRegistrationWasNotAlreadySubmitted() {

            history.firstOrNull { it is RegistrationRequestWasSubmitted && it.emailAddress == emailAddress }?.let { throw UserAlreadyRegisteredException(userId = id, emailAddress = emailAddress) }
        }

        private suspend fun publish(event: EntityEvent) = _events.publish(event)
    }
}

class UserAlreadyRegisteredException(val userId: SortableTimestampedUniqueIdentifier<*>, val emailAddress: EmailAddress) : IllegalStateException("User is already registered")

interface User : Entity<SortableTimestampedUniqueIdentifier<*>> {

    suspend fun submitRegistrationRequest()
}

interface Entity<ID> : Identifiable<ID> {

    val events: EntityEventStore
}

interface Application {

    suspend operator fun <RESULT> invoke(command: ApplicationCommand<RESULT>): RESULT
}

sealed interface ApplicationCommand<out RESULT> : Command

sealed class RegisterUserCommand {

    class V1(val command: RegisterUser.V1) : RegisterUserCommand(), ApplicationCommand<V1.Result>, RegisterUser by command {

        sealed interface Result {

            data object Accepted : Result

            sealed interface Rejected : Result {

                data class EmailAddressAlreadyInUse(val userId: SortableTimestampedUniqueIdentifier<*>) : Rejected
            }
        }
    }
}

fun RegisterUser.V1.asApplicationCommand() = RegisterUserCommand.V1(this)

interface RegisterUser : Command {

    val emailAddress: EmailAddress

    companion object {
        val typeName = "register-user".let(::Name)
    }

    class V1(override val emailAddress: EmailAddress) : RegisterUser {

        override val type: Command.Type get() = Type

        object Type : Command.Type {
            override val version = 1.let(::IntVersion)
            override val id = typeName
        }

        companion object
    }
}

interface Command : Instruction {

    override val type: Type

    companion object

    interface Type : Happening.Type {

        companion object
    }
}

interface Query : Instruction {

    override val type: Type

    companion object

    interface Type : Happening.Type {

        companion object
    }
}

sealed class RegistrationRequestWasSubmitted(val emailAddress: EmailAddress, userId: SortableTimestampedUniqueIdentifier<*>, override val id: SortableTimestampedUniqueIdentifier<*>, override val timestamp: Instant, type: Type) : UserEvent(id, timestamp, type, userId) {

    interface Type : UserEvent.Type

    companion object {
        val typeId = "registration-request-submitted".let(::Name)
    }

    class V1(emailAddress: EmailAddress, userId: SortableTimestampedUniqueIdentifier<*>, id: SortableTimestampedUniqueIdentifier<*>, timestamp: Instant) : RegistrationRequestWasSubmitted(emailAddress, userId, id, timestamp, Type) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as V1

            if (emailAddress != other.emailAddress) return false
            if (userId != other.userId) return false
            if (id != other.id) return false
            if (timestamp != other.timestamp) return false

            return true
        }

        override fun hashCode(): Int {
            var result = emailAddress.hashCode()
            result = 31 * result + userId.hashCode()
            result = 31 * result + id.hashCode()
            result = 31 * result + timestamp.hashCode()
            return result
        }

        override fun toString() = "RegistrationRequestWasSubmitted.V1(emailAddress=$emailAddress, userId=$userId, id=$id, timestamp=$timestamp)"

        object Type : RegistrationRequestWasSubmitted.Type {
            override val id get() = typeId
            override val version = 1.let(::IntVersion)
        }
    }
}

sealed class UserEvent(override val id: SortableTimestampedUniqueIdentifier<*>, override val timestamp: Instant, override val type: Type, val userId: SortableTimestampedUniqueIdentifier<*>) : EntityEvent {

    override val entityId = userId
    override val entityType: Name get() = ENTITY_TYPE

    companion object {
        private val ENTITY_TYPE = "user".let(::Name)
    }

    interface Type : EntityEvent.Type {

        companion object
    }
}

interface EntityEvent : Event {

    val entityId: SortableTimestampedUniqueIdentifier<*>
    val entityType: Name

    override val type: Type

    companion object

    interface Type : Event.Type {

        companion object
    }
}

interface Event : Happening, Identifiable<SortableTimestampedUniqueIdentifier<*>>, Timestamped {

    override val type: Type

    companion object

    interface Type : Happening.Type {

        companion object
    }
}

interface Instruction : Happening {

    companion object
}

interface Happening : Versioned<IntVersion> {

    val type: Type
    override val version: IntVersion get() = type.version

    companion object

    interface Type : Identifiable<Name>, Versioned<IntVersion> {

        companion object
    }
}