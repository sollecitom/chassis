package org.sollecitom.chassis.example.command_endpoint.application.user.registration

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.ddd.application.dispatching.CommandHandler
import org.sollecitom.chassis.ddd.domain.*
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
private class RegisterUserTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Nested
    inner class V1 {

        @Test
        fun `registering a user with an email address never used before`() = runTest {

            val emailAddress = "fresh-address@gmail.com".let(::EmailAddress)
            val command = RegisterUser.V1(emailAddress = emailAddress)
            val invocationContext = InvocationContext.unauthenticated()
            val handler = newV1Handler()

            val result = with(invocationContext) { handler.process(command) }

            assertThat(result).wasAccepted()
        }

        @Test
        fun `registering a user with an email address that was used before the command was received`() = runTest {

            val emailAddress = "stale-address@gmail.com".let(::EmailAddress)
            val command = RegisterUser.V1(emailAddress = emailAddress)
            val invocationContext = InvocationContext.unauthenticated()
            val anotherUser = User(id = newId.forEntities())
            val handler = newV1Handler { address -> address.takeIf { it == emailAddress }?.let { anotherUser } }

            val result = with(invocationContext) { handler.process(command) }

            assertThat(result).wasRejectedBecauseAnotherUserHasTheSameEmailAddress(user = anotherUser)
        }

        private fun newV1Handler(userWithEmailAddress: suspend context(InvocationContext<*>)(EmailAddress) -> User? = { null }): CommandHandler<RegisterUser.V1, RegisterUser.V1.Result, Access> = RegisterUserV1Handler(findUserWithEmailAddress = userWithEmailAddress, uniqueIdGenerator = this@RegisterUserTests)

        private fun Assert<RegisterUser.V1.Result>.wasAccepted() = given { result -> assertThat(result).isInstanceOf<Accepted>() }

        private fun Assert<RegisterUser.V1.Result>.wasRejectedBecauseAnotherUserHasTheSameEmailAddress(user: User) = given { result -> assertThat(result).isEqualTo(EmailAddressAlreadyInUse(user = user)) }
    }
}

class RegisterUserV1Handler(private val findUserWithEmailAddress: suspend context(InvocationContext<*>)(EmailAddress) -> User?, private val uniqueIdGenerator: UniqueIdGenerator) : CommandHandler<RegisterUser.V1, RegisterUser.V1.Result, Access>, UniqueIdGenerator by uniqueIdGenerator {
//class RegisterUserV1Handler(private val findUserWithEmailAddress: suspend context(InvocationContext<*>)(EmailAddress) -> User?, private val uniqueIdGenerator: UniqueIdGenerator) : ApplicationCommandHandler<RegisterUser.V1, RegisterUser.V1.Result, Access>, UniqueIdGenerator by uniqueIdGenerator {

    override val commandType get() = RegisterUser.V1.type

    context(InvocationContext<Access>)
    override suspend fun process(command: RegisterUser.V1): RegisterUser.V1.Result {

        // TODO subscribe to the result coming from downstream
        // TODO publish the event
        // TODO await the result


        val user = UserWithPendingRegistration(id = newId.forEntities())
        return Accepted(user = user)
    }

    context(InvocationContext<Access>)
    private suspend fun runPreChecks(command: RegisterUser.V1): RegisterUser.V1.Result? {

        val existingUserWithTheSameEmailAddress = findUserWithEmailAddress(this@InvocationContext, command.emailAddress)
        return existingUserWithTheSameEmailAddress?.let { EmailAddressAlreadyInUse(it) }
    }
}

// TODO move
sealed class CommandWasReceived<COMMAND : Command<*, *>>(val command: COMMAND, override val id: Id, override val timestamp: Instant, override val context: Event.Context) : Event {

    val commandType get() = command.type

    override val type get() = Companion.type

    companion object {
        private val type = Happening.Type("command-was-received".let(::Name), 1.let(::IntVersion))
    }
}

class EntitySpecificCommandWasReceived<COMMAND : EntityCommand<*, *>>(command: COMMAND, id: Id, timestamp: Instant, context: Event.Context) : CommandWasReceived<COMMAND>(command, id, timestamp, context), EntityEvent {

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

class GenericCommandWasReceived<COMMAND : Command<*, *>>(command: COMMAND, id: Id, timestamp: Instant, context: Event.Context) : CommandWasReceived<COMMAND>(command, id, timestamp, context) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenericCommandWasReceived<*>

        return id == other.id
    }

    override fun hashCode() = id.hashCode()

    override fun toString() = "GenericCommandWasReceived(command=$command, id=$id, timestamp=$timestamp, context=$context)"
}