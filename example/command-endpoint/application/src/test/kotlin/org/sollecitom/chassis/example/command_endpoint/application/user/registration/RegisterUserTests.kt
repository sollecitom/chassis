package org.sollecitom.chassis.example.command_endpoint.application.user.registration

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

            assertThat(result).isInstanceOf<RegisterUser.V1.Result.Accepted>()
        }

        @Test
        fun `registering a user with an email address that was used before the command was received`() = runTest {

            val emailAddress = "stale-address@gmail.com".let(::EmailAddress)
            val command = RegisterUser.V1(emailAddress = emailAddress)
            val invocationContext = InvocationContext.unauthenticated()
            val existingUserWithTheSameEmailAddress = User(id = newId.forEntities())
            val handler = newV1Handler { address -> address.takeIf { it == emailAddress }?.let { existingUserWithTheSameEmailAddress } }

            val result = with(invocationContext) { handler.process(command) }

            assertThat(result).isInstanceOf<RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse>()
            result as RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
            assertThat(result.user).isEqualTo(existingUserWithTheSameEmailAddress)
        }

        private fun newV1Handler(userWithEmailAddress: suspend (EmailAddress) -> User? = { null }): ApplicationCommandHandler<RegisterUser.V1, RegisterUser.V1.Result, Access> = RegisterUserV1Handler(userWithEmailAddress = userWithEmailAddress, uniqueIdGenerator = this@RegisterUserTests)
    }
}

class RegisterUserV1Handler(private val userWithEmailAddress: suspend (EmailAddress) -> User?, private val uniqueIdGenerator: UniqueIdGenerator) : ApplicationCommandHandler<RegisterUser.V1, RegisterUser.V1.Result, Access>, UniqueIdGenerator by uniqueIdGenerator {

    override val commandType get() = RegisterUser.V1.type

    context(InvocationContext<Access>)
    override suspend fun process(command: RegisterUser.V1): RegisterUser.V1.Result {

        val existingUserWithTheSameEmailAddress = userWithEmailAddress(command.emailAddress)
        if (existingUserWithTheSameEmailAddress != null) {
            return RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse(user = existingUserWithTheSameEmailAddress)
        }
        val user = UserWithPendingRegistration(id = newId.forEntities())
        return RegisterUser.V1.Result.Accepted(user = user)
    }
}