package org.sollecitom.chassis.example.service.endpoint.write.application

import assertk.assertThat
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreUtils
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.ddd.test.utils.InMemoryEventStore
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
import org.sollecitom.chassis.example.service.endpoint.write.configuration.configureLogging
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRepository

@TestInstance(PER_CLASS)
private class ApplicationTests : WithCoreUtils by WithCoreUtils.testProvider {

    init {
        configureLogging()
    }

    @Nested
    inner class UserTests {

        @Test
        fun `registering a new user`() = runTest {

            val application = newApplication()
            val emailAddress = "someone@somedomain.com".let(::EmailAddress)
            val registerUser = registerUser(emailAddress = emailAddress)

            val result = application(registerUser)

            assertThat(result).isInstanceOf<Accepted>()
        }

        @Test
        fun `a user attempting to register with an email address already in use`() = runTest {

            val application = newApplication()
            val emailAddress = "someone@somedomain.com".let(::EmailAddress)
            val registerUserAFirstTime = registerUser(emailAddress = emailAddress)
            application(registerUserAFirstTime).let { check(it is Accepted) }

            val registerUserASecondTime = registerUser(emailAddress = emailAddress)
            val result = application(registerUserASecondTime)

            assertThat(result).isInstanceOf<EmailAddressAlreadyInUse>()
        }

        private fun registerUser(emailAddress: EmailAddress) = RegisterUser.V1(emailAddress = emailAddress)
    }

    private fun newApplication(events: EventStore.Mutable = InMemoryEventStore(), userRepository: UserRepository = InMemoryUserRepository(events = events, coreUtilsAdapter = this)): Application = Application(userRepository::withEmailAddress)
}