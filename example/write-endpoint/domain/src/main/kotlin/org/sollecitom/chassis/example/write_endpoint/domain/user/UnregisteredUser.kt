package org.sollecitom.chassis.example.write_endpoint.domain.user

import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.TimeGenerator
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.PublishedEvent
import org.sollecitom.chassis.ddd.domain.toEventContext
import org.sollecitom.chassis.example.event.domain.user.UserEvent
import org.sollecitom.chassis.example.event.domain.user.registration.UserRegistrationRequestWasSubmitted

context(UniqueIdGenerator, TimeGenerator)
internal class UnregisteredUser(override val id: Id, private val emailAddress: EmailAddress, private val publish: suspend (UserEvent) -> Deferred<Unit>) : User {

    context(InvocationContext<*>)
    override suspend fun submitRegistrationRequest(): PublishedEvent<UserRegistrationRequestWasSubmitted.V1> {

        val event = registrationRequestWasSubmitted()
        val wasPersisted = publish(event)
        return PublishedEvent(event, wasPersisted)
    }

    context(InvocationContext<*>)
    private fun registrationRequestWasSubmitted(): UserRegistrationRequestWasSubmitted.V1 {

        val now = clock.now()
        return UserRegistrationRequestWasSubmitted.V1(emailAddress = emailAddress, userId = id, id = newId.internal(now), timestamp = now, context = this@InvocationContext.toEventContext())
    }
}