package org.sollecitom.chassis.example.write_endpoint.adapters.driven.user.repository

import assertk.assertThat
import assertk.assertions.isBetween
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinx.coroutines.CoroutineScope
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
import org.sollecitom.chassis.example.event.domain.UserEvent
import org.sollecitom.chassis.example.event.domain.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.event.domain.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.example.write_endpoint.domain.user.EventSourcedUserRepository
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class InMemoryEventSourcedEntitiesTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    private val timeout = 10.seconds

    @Test
    fun `an unregistered user submitting a register user command`() = runTest(timeout = timeout) {

        val events = eventFramework()
        val users = EventSourcedUserRepository(events = events, coreDataGenerators = this@InMemoryEventSourcedEntitiesTests)
        val emailAddress = "bruce@waynecorps.com".let(::EmailAddress)

        val user = users.withEmailAddress(emailAddress)
        val invocationContext = InvocationContext.create()
        val beforeTheInvocation = clock.now()
        val (event, wasPersisted) = with(invocationContext) { user.submitRegistrationRequest() }
        wasPersisted.join()
        val afterTheInvocation = clock.now()

        val publishedEvent = events.lastOrNull()
        assertThat(event).isEqualTo(publishedEvent)
        assertThat(event).isInstanceOf<UserRegistrationRequestWasSubmitted.V1>().given {
            assertThat(it.emailAddress).isEqualTo(emailAddress)
            assertThat(it.entityId).isEqualTo(it.userId)
            assertThat(it.timestamp).isBetween(beforeTheInvocation, afterTheInvocation)
            assertThat(it).hasInvocationContext(invocationContext)
            assertThat(it).isOriginating()
        }
    }

    @Test
    fun `an already registered user submitting a register user command`() = runTest(timeout = timeout) {

        val events = eventFramework()
        val users = EventSourcedUserRepository(events = events, coreDataGenerators = this@InMemoryEventSourcedEntitiesTests)
        val emailAddress = "lucious@waynecorps.com".let(::EmailAddress)
        with(InvocationContext.create()) { users.withEmailAddress(emailAddress).submitRegistrationRequest() }

        val user = users.withEmailAddress(emailAddress)
        val invocationContext = InvocationContext.create()
        val beforeTheInvocation = clock.now()
        val (event, wasPersisted) = with(invocationContext) { user.submitRegistrationRequest() }
        val afterTheInvocation = clock.now()
        wasPersisted.join()

        val publishedEvent = events.lastOrNull()
        assertThat(event).isEqualTo(publishedEvent)
        assertThat(event).isInstanceOf<UserRegistrationRequestWasAlreadySubmitted.V1>().given {
            assertThat(it.emailAddress).isEqualTo(emailAddress)
            assertThat(it.entityId).isEqualTo(it.userId)
            assertThat(it.timestamp).isBetween(beforeTheInvocation, afterTheInvocation)
            assertThat(it).hasInvocationContext(invocationContext)
            assertThat(it).isOriginating()
        }
    }

    context(CoroutineScope)
    private fun eventFramework(): EventFramework.Mutable = EventFramework.Mutable.inMemory(queryFactory = UserEvent.inMemoryQueryFactory)
}