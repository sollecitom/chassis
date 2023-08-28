package org.sollecitom.chassis.example.service.endpoint.write.application

import assertk.assertThat
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.authenticated
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.ddd.test.utils.InMemoryEventStore
import org.sollecitom.chassis.ddd.test.utils.InMemoryEventStoreQuery
import org.sollecitom.chassis.ddd.test.utils.InMemoryQueryFactory
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
import org.sollecitom.chassis.example.service.endpoint.write.configuration.configureLogging
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRepository
import org.sollecitom.chassis.test.utils.assertions.failedThrowing
import kotlin.reflect.KClass

@TestInstance(PER_CLASS)
private class ApplicationTests : WithCoreGenerators by WithCoreGenerators.testProvider {

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

            val result = application(registerUser, context)

            assertThat(result).isInstanceOf<Accepted>()
        }

        @Test
        fun `attempting to register a new user with an authenticated invocation context`() = runTest {

            val application = newApplication()
            val emailAddress = "someone@somedomain.com".let(::EmailAddress)
            val registerUser = registerUser(emailAddress = emailAddress)
            val context = InvocationContext.authenticated()

            val attempt = runCatching { application(registerUser, context) }

            assertThat(attempt).failedThrowing<IllegalStateException>()
        }

        @Test
        fun `a user attempting to register with an email address already in use`() = runTest {

            val application = newApplication()
            val emailAddress = "someone@somedomain.com".let(::EmailAddress)
            val registerUserAFirstTime = registerUser(emailAddress = emailAddress)
            application(registerUserAFirstTime, InvocationContext.unauthenticated()).let { check(it is Accepted) }

            val registerUserASecondTime = registerUser(emailAddress = emailAddress)
            val result = application(registerUserASecondTime, InvocationContext.unauthenticated())

            assertThat(result).isInstanceOf<EmailAddressAlreadyInUse>()
        }

        private fun registerUser(emailAddress: EmailAddress) = RegisterUser.V1(emailAddress = emailAddress)
    }

    private fun newApplication(events: EventStore.Mutable = InMemoryEventStore(queryFactory = ApplicationEventQueryFactory), userRepository: UserRepository = InMemoryUserRepository(events = events, coreGenerators = this)): Application = Application(userRepository::withEmailAddress)
}

// TODO move
object ApplicationEventQueryFactory : InMemoryQueryFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <IN_MEMORY_QUERY : InMemoryEventStoreQuery<QUERY, EVENT>, QUERY : EventStore.Query<EVENT>, EVENT : Event> invoke(query: QUERY): IN_MEMORY_QUERY? = when (query) {
        is EventStore.Query.Unrestricted -> InMemoryApplicationEventQuery.Unrestricted as IN_MEMORY_QUERY
        is ApplicationEventQuery.UserRegistrationWithEmailAddress -> InMemoryApplicationEventQuery.UserRegistrationWithEmailAddress(query) as IN_MEMORY_QUERY
        else -> null
    }
}

// TODO move
sealed interface InMemoryApplicationEventQuery<QUERY : EventStore.Query<EVENT>, EVENT : Event> : InMemoryEventStoreQuery<QUERY, EVENT> {

    class UserRegistrationWithEmailAddress(private val delegate: ApplicationEventQuery.UserRegistrationWithEmailAddress) : InMemoryApplicationEventQuery<ApplicationEventQuery.UserRegistrationWithEmailAddress, UserRegistrationRequestWasSubmitted> {

        override val eventType: KClass<UserRegistrationRequestWasSubmitted> get() = UserRegistrationRequestWasSubmitted::class

        override fun invoke(event: UserRegistrationRequestWasSubmitted) = event.emailAddress == delegate.emailAddress
    }

    data object Unrestricted : InMemoryApplicationEventQuery<EventStore.Query.Unrestricted, Event> {

        override val eventType: KClass<Event> get() = Event::class

        override fun invoke(event: Event) = true
    }
}

// TODO move
sealed interface ApplicationEventQuery<EVENT : Event> : EventStore.Query<EVENT> {

    class UserRegistrationWithEmailAddress(val emailAddress: EmailAddress) : ApplicationEventQuery<UserRegistrationRequestWasSubmitted> {

        override val type: Name = Companion.type

        companion object {
            private val type = "user-registration-event-with-email-address".let(::Name)
        }
    }
}