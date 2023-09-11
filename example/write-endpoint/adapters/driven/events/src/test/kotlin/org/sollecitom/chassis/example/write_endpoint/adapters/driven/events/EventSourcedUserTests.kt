package org.sollecitom.chassis.example.write_endpoint.adapters.driven.events

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.CoroutineStart.UNDISPATCHED
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.create
import org.sollecitom.chassis.ddd.domain.framework.EventFramework
import org.sollecitom.chassis.ddd.event.framework.memory.inMemory
import org.sollecitom.chassis.ddd.test.utils.hasInvocationContext
import org.sollecitom.chassis.ddd.test.utils.isOriginating
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRegistrationRequestWasSubmitted
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class EventSourcedEntitiesTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    private val timeout = 10.seconds

    @Test
    fun `an unregistered user submitting a register user command`() = runTest(timeout = timeout) {

        val events = eventFramework()
        val users = EventSourcedUserRepository(events = events, coreDataGenerators = this@EventSourcedEntitiesTests)
        val emailAddress = "bruce@waynecorps.com".let(::EmailAddress)
        val user = users.withEmailAddress(emailAddress)
        val invocationContext = InvocationContext.create()

        val receivingThePublishedEvent = async(start = UNDISPATCHED) { events.asFlow.first() }
        val event = with(invocationContext) { user.submitRegistrationRequest() }

        val publishedEvent = receivingThePublishedEvent.await()
        assertThat(event).isEqualTo(publishedEvent)
        assertThat(event).isInstanceOf<UserRegistrationRequestWasSubmitted.V1>().given {

            assertThat(it.emailAddress).isEqualTo(emailAddress)
            assertThat(it.entityId).isEqualTo(it.userId)
            assertThat(it).hasInvocationContext(invocationContext)
            assertThat(it).isOriginating()
        }
    }

    @Test
    fun `an already registered user submitting a register user command`() = runTest(timeout = timeout) {

        val events = eventFramework()
        val users = EventSourcedUserRepository(events = events, coreDataGenerators = this@EventSourcedEntitiesTests)
        val emailAddress = "lucious@waynecorps.com".let(::EmailAddress)
        val previousInvocationContext = InvocationContext.create()
        with(previousInvocationContext) { users.withEmailAddress(emailAddress).submitRegistrationRequest() }
        val invocationContext = InvocationContext.create()

        testScheduler.advanceUntilIdle()
        val user = users.withEmailAddress(emailAddress)
        val receivingThePublishedEvent = async(start = UNDISPATCHED) { events.asFlow.first() }
        val event = with(invocationContext) { user.submitRegistrationRequest() }

        val publishedEvent = receivingThePublishedEvent.await()
        assertThat(event).isEqualTo(publishedEvent)
        assertThat(event).isInstanceOf<UserRegistrationRequestWasAlreadySubmitted.V1>().given {

            assertThat(it.emailAddress).isEqualTo(emailAddress)
            assertThat(it.entityId).isEqualTo(it.userId)
            assertThat(it).hasInvocationContext(invocationContext)
            assertThat(it).isOriginating()
        }
    }

    context(TestScope)
    private fun eventFramework(): EventFramework.Mutable = EventFramework.Mutable.inMemory(queryFactory = UserEventQueryFactory, scope = this@TestScope)
}