package org.sollecitom.chassis.example.command_endpoint.application.user.registration

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.ddd.application.dispatching.ApplicationCommandHandler
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

        private fun newV1Handler(userWithEmailAddress: suspend context(InvocationContext<*>)(EmailAddress) -> User? = { null }): ApplicationCommandHandler<RegisterUser.V1, RegisterUser.V1.Result, Access> = RegisterUserV1Handler(findUserWithEmailAddress = userWithEmailAddress, uniqueIdGenerator = this@RegisterUserTests)

        private fun Assert<RegisterUser.V1.Result>.wasAccepted() = given { result -> assertThat(result).isInstanceOf<Accepted>() }

        private fun Assert<RegisterUser.V1.Result>.wasRejectedBecauseAnotherUserHasTheSameEmailAddress(user: User) = given { result -> assertThat(result).isEqualTo(EmailAddressAlreadyInUse(user = user)) }
    }
}

class RegisterUserV1Handler(private val findUserWithEmailAddress: suspend context(InvocationContext<*>)(EmailAddress) -> User?, private val uniqueIdGenerator: UniqueIdGenerator) : ApplicationCommandHandler<RegisterUser.V1, RegisterUser.V1.Result, Access>, UniqueIdGenerator by uniqueIdGenerator {

    override val commandType get() = RegisterUser.V1.type

    context(InvocationContext<Access>)
    override suspend fun process(command: RegisterUser.V1): RegisterUser.V1.Result {

        runPreChecks(command)?.let { return it }

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