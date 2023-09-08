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
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.authenticated
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.domain.Events
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEvents
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.memory.EventSourcedUserRepository
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.memory.UserEventQueryFactory
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
import org.sollecitom.chassis.example.service.endpoint.write.configuration.configureLogging
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRepository
import org.sollecitom.chassis.test.utils.assertions.failedThrowing

@TestInstance(PER_CLASS)
private class ApplicationTests : CoreDataGenerator by CoreDataGenerator.testProvider {

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
            val context = InvocationContext.unauthenticated()

            val result = with(context) { application(registerUser) }

            assertThat(result).isInstanceOf<Accepted>()
        }

        @Test
        fun `attempting to register a new user with an authenticated invocation context`() = runTest {

            val application = newApplication()
            val emailAddress = "someone@somedomain.com".let(::EmailAddress)
            val registerUser = registerUser(emailAddress = emailAddress)
            val context = InvocationContext.authenticated()

            val attempt = runCatching { with(context) { application(registerUser) } }

            assertThat(attempt).failedThrowing<IllegalStateException>()
        }

        @Test
        fun `a user attempting to register with an email address already in use`() = runTest {

            val application = newApplication()
            val emailAddress = "someone@somedomain.com".let(::EmailAddress)
            val registerUserAFirstTime = registerUser(emailAddress = emailAddress)
            with(InvocationContext.unauthenticated()) { application(registerUserAFirstTime) }.let { check(it is Accepted) }

            val registerUserASecondTime = registerUser(emailAddress = emailAddress)
            val result = with(InvocationContext.unauthenticated()) { application(registerUserASecondTime) }

            assertThat(result).isInstanceOf<EmailAddressAlreadyInUse>()
        }

        private fun registerUser(emailAddress: EmailAddress) = RegisterUser.V1(emailAddress = emailAddress)
    }

    // TODO remove the in-memory adapter from here - use a stub instead
    private fun newApplication(events: Events.Mutable = InMemoryEvents(queryFactory = UserEventQueryFactory), userRepository: UserRepository = EventSourcedUserRepository(events = events, coreDataGenerators = this)): Application = Application(userRepository::withEmailAddress)
}