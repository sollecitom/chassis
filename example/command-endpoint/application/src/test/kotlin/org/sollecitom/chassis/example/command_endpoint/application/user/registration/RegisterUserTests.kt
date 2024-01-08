package org.sollecitom.chassis.example.command_endpoint.application.user.registration

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.TimeGenerator
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.ddd.application.dispatching.CommandHandler
import org.sollecitom.chassis.ddd.domain.*
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.RegisterUser.Result.Accepted
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.RegisterUser.Result.Rejected.EmailAddressAlreadyInUse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
private class RegisterUserTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `registering a user with an email address never used before`() = runTest {

        val emailAddress = "fresh-address@gmail.com".let(::EmailAddress)
        val command = RegisterUser(emailAddress = emailAddress)
        val invocationContext = InvocationContext.unauthenticated()
        val handler = newHandler()

        val result = with(invocationContext) { handler.process(command) }

        assertThat(result).wasAccepted()
    }

    @Test
    fun `registering a user with an email address that was used before the command was received`() = runTest {

        val emailAddress = "stale-address@gmail.com".let(::EmailAddress)
        val command = RegisterUser(emailAddress = emailAddress)
        val invocationContext = InvocationContext.unauthenticated()
        val anotherUser = User(id = newId.forEntities())
        val handler = newHandler { address: EmailAddress -> address.takeIf { it == emailAddress }?.let { anotherUser } }

        val result = with(invocationContext) { handler.process(command) }

        assertThat(result).wasRejectedBecauseAnotherUserHasTheSameEmailAddress(user = anotherUser)
    }

    private fun newHandler(process: suspend context(InvocationContext<Access>)(CommandWasReceived<RegisterUser>) -> RegisterUser.Result): CommandHandler<RegisterUser, RegisterUser.Result, Access> {

        val processor = InMemoryCommandProcessor(process)
        return RegisterUserHandler(receivedCommandPublisher = processor, commandResultSubscriber = processor, uniqueIdGenerator = this@RegisterUserTests, timeGenerator = this@RegisterUserTests)
    }

    private fun newHandler(findExistingUser: suspend (EmailAddress) -> User? = { null }) = newHandler(process = { event ->

        val existingUser = findExistingUser(event.command.emailAddress)
        existingUser?.let { EmailAddressAlreadyInUse(user = it) } ?: Accepted(user = UserWithPendingRegistration(id = newId.forEntities()))
    })

    private fun Assert<RegisterUser.Result>.wasAccepted() = given { result -> assertThat(result).isInstanceOf<Accepted>() }

    private fun Assert<RegisterUser.Result>.wasRejectedBecauseAnotherUserHasTheSameEmailAddress(user: User) = given { result -> assertThat(result).isEqualTo(EmailAddressAlreadyInUse(user = user)) }
}

class RegisterUserHandler(private val receivedCommandPublisher: ReceivedCommandPublisher<RegisterUser, Access>, private val commandResultSubscriber: CommandResultSubscriber<RegisterUser, RegisterUser.Result, Access>, private val uniqueIdGenerator: UniqueIdGenerator, private val timeGenerator: TimeGenerator) : CommandHandler<RegisterUser, RegisterUser.Result, Access>, UniqueIdGenerator by uniqueIdGenerator, TimeGenerator by timeGenerator {

    override val commandType get() = RegisterUser.type

    context(InvocationContext<Access>)
    override suspend fun process(command: RegisterUser): RegisterUser.Result = coroutineScope {

        val event = command.wasReceived()
        val result = event.deferredResult()
        event.publish()
        result.await()
    }

    context(InvocationContext<Access>)
    private fun CommandWasReceived<RegisterUser>.deferredResult() = commandResultSubscriber.resultForCommand(this)

    context(InvocationContext<Access>)
    private suspend fun GenericCommandWasReceived<RegisterUser>.publish() = receivedCommandPublisher.publish(this)
}

class InMemoryCommandProcessor<in COMMAND : Command<RESULT, ACCESS>, out RESULT : Any, out ACCESS : Access>(private val process: suspend context(InvocationContext<ACCESS>)(CommandWasReceived<COMMAND>) -> RESULT) : ReceivedCommandPublisher<COMMAND, ACCESS>, CommandResultSubscriber<COMMAND, RESULT, ACCESS> {

    private val results = mutableMapOf<CommandWasReceived<COMMAND>, CompletableDeferred<RESULT>>()

    context(InvocationContext<ACCESS>)
    override suspend fun publish(event: CommandWasReceived<COMMAND>) {

        val result = process(this@InvocationContext, event)
        results.remove(event)?.complete(result)
    }

    context(InvocationContext<ACCESS>)
    override fun resultForCommand(event: CommandWasReceived<COMMAND>): Deferred<RESULT> {

        val result = CompletableDeferred<RESULT>()
        results[event] = result
        return result
    }
}

// TODO move
interface ReceivedCommandPublisher<in COMMAND : Command<*, ACCESS>, out ACCESS : Access> {

    context(InvocationContext<ACCESS>)
    suspend fun publish(event: CommandWasReceived<COMMAND>)
}

interface CommandResultSubscriber<in COMMAND : Command<RESULT, ACCESS>, out RESULT : Any, out ACCESS : Access> {

    context(InvocationContext<ACCESS>)
    fun resultForCommand(event: CommandWasReceived<COMMAND>): Deferred<RESULT>
}

sealed class CommandWasReceived<out COMMAND : Command<*, *>>(val command: COMMAND, override val id: Id, override val timestamp: Instant, override val context: Event.Context) : Event {

    val commandType get() = command.type

    override val type get() = Companion.type

    companion object {
        private val type = Happening.Type("command-was-received".let(::Name), 1.let(::IntVersion))
    }
}

class EntitySpecificCommandWasReceived<out COMMAND : EntityCommand<*, *>>(command: COMMAND, id: Id, timestamp: Instant, context: Event.Context) : CommandWasReceived<COMMAND>(command, id, timestamp, context), EntityEvent {

    override val entityId get() = command.entityId
    override val entityType get() = command.entityType

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntitySpecificCommandWasReceived<*>

        return id == other.id
    }

    override fun hashCode() = id.hashCode()

    override fun toString() = "EntitySpecificCommandWasReceived(command=$command, id=$id, timestamp=$timestamp, context=$context)"
}

class GenericCommandWasReceived<out COMMAND : Command<*, *>>(command: COMMAND, id: Id, timestamp: Instant, context: Event.Context) : CommandWasReceived<COMMAND>(command, id, timestamp, context) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenericCommandWasReceived<*>

        return id == other.id
    }

    override fun hashCode() = id.hashCode()

    override fun toString() = "GenericCommandWasReceived(command=$command, id=$id, timestamp=$timestamp, context=$context)"
}

context(InvocationContext<Access>, UniqueIdGenerator, TimeGenerator)
fun <COMMAND : Command<*, *>> COMMAND.wasReceived(): GenericCommandWasReceived<COMMAND> {

    val id = newId.forEntities()
    val timestamp = clock.now()
    val context = Event.Context(invocation = this@InvocationContext)
    return GenericCommandWasReceived(this, id, timestamp, context)
}

context(InvocationContext<Access>, UniqueIdGenerator, TimeGenerator)
fun <COMMAND : EntityCommand<*, *>> COMMAND.wasReceived(): EntitySpecificCommandWasReceived<COMMAND> {

    val id = newId.forEntities()
    val timestamp = clock.now()
    val context = Event.Context(invocation = this@InvocationContext)
    return EntitySpecificCommandWasReceived(this, id, timestamp, context)
}