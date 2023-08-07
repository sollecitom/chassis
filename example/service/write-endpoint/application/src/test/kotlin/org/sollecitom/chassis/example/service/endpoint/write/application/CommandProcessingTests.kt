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
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.traits.Identifiable
import org.sollecitom.chassis.core.domain.traits.Timestamped
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.core.domain.versioning.Versioned
import org.sollecitom.chassis.example.service.endpoint.write.application.RegisterUserCommandV1.Result.Accepted
import org.sollecitom.chassis.example.service.endpoint.write.application.RegisterUserCommandV1.Result.Rejected.EmailAddressAlreadyInUse

@TestInstance(PER_CLASS)
private class CommandProcessingTests {

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
        val registerUser = registerUser(emailAddress = emailAddress).asApplicationCommand()
        application(registerUser).let { check(it is Accepted) }

        val registerUserWithSameEmail = registerUser(emailAddress = emailAddress).asApplicationCommand()

        val result = application(registerUserWithSameEmail)

        assertThat(result).isInstanceOf<EmailAddressAlreadyInUse>()
    }

    private fun registerUser(emailAddress: EmailAddress): RegisterUser.V1 {

        return RegisterUser.V1(emailAddress = emailAddress)
    }

    private fun newApplication(events: EventStore.Mutable = InMemoryEventStore(), clock: Clock = Clock.System, userRepository: UserRepository = InMemoryUserRepository(events = events, newId = newULID::invoke, clock = clock)): Application = DispatchingApplication(userRepository::withEmailAddress)
}

class DispatchingApplication(private val userWithEmailAddress: suspend (EmailAddress) -> User) : Application {

    @Suppress("UNCHECKED_CAST")
    override suspend fun <RESULT> invoke(command: ApplicationCommand<RESULT>) = when (command) {
        is RegisterUserCommandV1 -> process(command) as RESULT
    }

    private suspend fun process(command: RegisterUserCommandV1): RegisterUserCommandV1.Result {

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

    fun forEntity(entityId: SortableTimestampedUniqueIdentifier<*>): EntityEventStore

    fun history(): Flow<Event>

    val stream: Flow<Event>

    interface Mutable : EventStore {

        suspend fun publish(event: Event)

        override fun forEntity(entityId: SortableTimestampedUniqueIdentifier<*>): EntityEventStore.Mutable
    }
}

interface EntityEventStore {

    fun history(): Flow<EntityEvent>

    val stream: Flow<EntityEvent>

    interface Mutable : EntityEventStore {

        suspend fun publish(event: EntityEvent)
    }
}

class InMemoryEventStore : EventStore.Mutable {

    private val history = mutableListOf<Event>()
    private val _stream = MutableSharedFlow<Event>()
    private val mutex = Mutex()

    override suspend fun publish(event: Event) = mutex.withLock {
        history += event
        _stream.emit(event)
    }

    override fun history() = history.asFlow()

    override val stream: Flow<Event> get() = _stream

    override fun forEntity(entityId: SortableTimestampedUniqueIdentifier<*>): EntityEventStore.Mutable = EntityEventStoreView(entityId)

    private inner class EntityEventStoreView(private val entityId: SortableTimestampedUniqueIdentifier<*>) : EntityEventStore.Mutable {

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

class InMemoryUserRepository(private val events: EventStore.Mutable, private val newId: (Instant) -> SortableTimestampedUniqueIdentifier<*>, private val clock: Clock) : UserRepository {

    private val userByEmail = mutableMapOf<EmailAddress, User>()

    override suspend fun withEmailAddress(emailAddress: EmailAddress) = userByEmail.computeIfAbsent(emailAddress, ::createNewUser)

    private fun createNewUser(emailAddress: EmailAddress): User {

        val userId = newULID()
        return InMemoryUser(_events = events.forEntity(userId), id = userId, emailAddress = emailAddress, newId = newId, clock = clock)
    }

    private class InMemoryUser(override val id: SortableTimestampedUniqueIdentifier<*>, private val emailAddress: EmailAddress, private val _events: EntityEventStore.Mutable, private val newId: (Instant) -> SortableTimestampedUniqueIdentifier<*>, private val clock: Clock) : User {

        override val events: EntityEventStore get() = _events

        override suspend fun submitRegistrationRequest() {

            // TODO fail if an event is already there
            _events.history().firstOrNull { it is RegistrationRequestWasSubmittedV1 }
            val event = registrationRequestWasSubmitted()
            _events.publish(event)
        }

        private fun registrationRequestWasSubmitted(): RegistrationRequestWasSubmittedV1 {

            val now = clock.now()
            return RegistrationRequestWasSubmittedV1(emailAddress = emailAddress, userId = id, id = newId(now), timestamp = now)
        }
    }
}

class UserAlreadyRegisteredException : IllegalStateException("User is already registered")

interface User : Entity<SortableTimestampedUniqueIdentifier<*>> {

    suspend fun submitRegistrationRequest()
}

interface Entity<ID : SortableTimestampedUniqueIdentifier<*>> : Identifiable<ID> {

    val events: EntityEventStore
}

interface Application {

    suspend operator fun <RESULT> invoke(command: ApplicationCommand<RESULT>): RESULT
}

sealed interface ApplicationCommand<out RESULT> : Command

data class RegisterUserCommandV1(val command: RegisterUser.V1) : ApplicationCommand<RegisterUserCommandV1.Result>, RegisterUser by command {

    sealed interface Result {

        data object Accepted : Result

        sealed interface Rejected : Result {

            data class EmailAddressAlreadyInUse(val userId: SortableTimestampedUniqueIdentifier<*>) : Rejected
        }
    }
}

fun RegisterUser.V1.asApplicationCommand() = RegisterUserCommandV1(this)

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

class RegistrationRequestWasSubmittedV1(val emailAddress: EmailAddress, userId: SortableTimestampedUniqueIdentifier<*>, override val id: SortableTimestampedUniqueIdentifier<*>, override val timestamp: Instant) : UserEvent(id, timestamp, Type, userId) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegistrationRequestWasSubmittedV1

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

    override fun toString() = "RegistrationRequestWasSubmittedV1(emailAddress=$emailAddress, userId=$userId, id=$id, timestamp=$timestamp)"

    object Type : UserEvent.Type {
        override val id = "registration-request-submitted".let(::Name)
        override val version = 1.let(::IntVersion)
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

// TODO move
private val newULID = UniqueIdFactory().ulid
private val clock: Clock = Clock.System