package org.sollecitom.chassis.example.write_endpoint.application

import assertk.assertThat
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.authenticated
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.PublishedEvent
import org.sollecitom.chassis.ddd.test.utils.create
import org.sollecitom.chassis.example.event.domain.UserRegistrationEvent
import org.sollecitom.chassis.example.event.domain.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.event.domain.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.example.write_endpoint.application.user.RegisterUser
import org.sollecitom.chassis.example.write_endpoint.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.write_endpoint.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
import org.sollecitom.chassis.example.write_endpoint.domain.user.User
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
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

            val emailAddress = "someone@somedomain.com".let(::EmailAddress)
            val application = newApplication(user = unregisteredUser(emailAddress))
            val registerUser = registerUser(emailAddress = emailAddress)
            val context = InvocationContext.unauthenticated()

            val result = with(context) { application(registerUser) }

            assertThat(result).isInstanceOf<Accepted>()
        }

        @Test
        fun `attempting to register a new user with an authenticated invocation context`() = runTest {

            val emailAddress = "someone@somedomain.com".let(::EmailAddress)
            val application = newApplication(user = unregisteredUser(emailAddress))
            val registerUser = registerUser(emailAddress = emailAddress)
            val context = InvocationContext.authenticated()

            val attempt = runCatching { with(context) { application(registerUser) } }

            assertThat(attempt).failedThrowing<IllegalStateException>()
        }

        @Test
        fun `a user attempting to register with an email address already in use`() = runTest {

            val emailAddress = "someone@somedomain.com".let(::EmailAddress)
            val application = newApplication(user = alreadyRegisteredUser(emailAddress))

            val registerUserASecondTime = registerUser(emailAddress = emailAddress)
            val result = with(InvocationContext.unauthenticated()) { application(registerUserASecondTime) }

            assertThat(result).isInstanceOf<EmailAddressAlreadyInUse>()
        }

        private fun registerUser(emailAddress: EmailAddress) = RegisterUser.V1(emailAddress = emailAddress)
    }

    fun alreadyRegisteredUser(emailAddress: EmailAddress, id: Id = newId.internal(), event: UserRegistrationRequestWasAlreadySubmitted = UserRegistrationRequestWasAlreadySubmitted.V1(emailAddress, id, newId.internal(), clock.now(), Event.Context.create())): User = StubbedUser(id, event)

    fun unregisteredUser(emailAddress: EmailAddress, id: Id = newId.internal(), event: UserRegistrationRequestWasSubmitted = UserRegistrationRequestWasSubmitted.V1(emailAddress, id, newId.internal(), clock.now(), Event.Context.create())): User = StubbedUser(id, event)

    private class StubbedUser(override val id: Id, private val event: UserRegistrationEvent) : User {

        context(InvocationContext<*>)
        override suspend fun submitRegistrationRequest(): PublishedEvent<UserRegistrationEvent> {

            val wasPersisted = CompletableDeferred(Unit)
            return PublishedEvent(event, wasPersisted)
        }
    }

    private fun TestScope.newApplication(user: User): Application = Application { user }
}