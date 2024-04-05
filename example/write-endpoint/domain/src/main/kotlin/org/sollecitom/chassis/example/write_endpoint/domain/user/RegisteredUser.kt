package org.sollecitom.chassis.example.write_endpoint.domain.user

import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.TimeGenerator
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.PublishedEvent
import org.sollecitom.chassis.ddd.domain.toEventContext
import org.sollecitom.chassis.example.event.domain.user.UserEvent
import org.sollecitom.chassis.example.event.domain.user.registration.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.event.domain.user.registration.UserRegistrationRequestWasSubmitted

context(UniqueIdGenerator, TimeGenerator)
internal class RegisteredUser(private val pastUserRegistrationRequest: UserRegistrationRequestWasSubmitted, private val publish: suspend (UserEvent) -> Deferred<Unit>) : User {

    override val id: Id get() = pastUserRegistrationRequest.userId

    context(InvocationContext<*>)
    override suspend fun submitRegistrationRequest(): PublishedEvent<UserRegistrationRequestWasAlreadySubmitted.V1> {

        val event = pastUserRegistrationRequest.alreadySubmitted()
        val wasPersisted = publish(event)
        return PublishedEvent(event, wasPersisted)
    }

    context(InvocationContext<*>)
    private fun UserRegistrationRequestWasSubmitted.alreadySubmitted() = org.sollecitom.chassis.example.event.domain.user.registration.UserRegistrationRequestWasAlreadySubmitted.V1(emailAddress = emailAddress, userId = userId, id = newId.internal(), timestamp = clock.now(), context = this@InvocationContext.toEventContext())
}