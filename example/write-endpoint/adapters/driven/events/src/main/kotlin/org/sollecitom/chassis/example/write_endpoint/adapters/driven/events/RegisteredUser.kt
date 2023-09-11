package org.sollecitom.chassis.example.write_endpoint.adapters.driven.events

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.toEventContext
import org.sollecitom.chassis.example.write_endpoint.domain.user.User
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserEvent
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRegistrationRequestWasSubmitted

context(CoreDataGenerator)
internal class RegisteredUser(private val pastUserRegistrationRequest: UserRegistrationRequestWasSubmitted, private val publish: suspend (UserEvent) -> Unit) : User {

    override val id: Id get() = pastUserRegistrationRequest.userId

    context(InvocationContext<*>)
    override suspend fun submitRegistrationRequest(): UserRegistrationRequestWasAlreadySubmitted.V1 {

        val event = pastUserRegistrationRequest.alreadySubmitted()
        publish(event)
        return event
    }

    context(InvocationContext<*>)
    private fun UserRegistrationRequestWasSubmitted.alreadySubmitted() = UserRegistrationRequestWasAlreadySubmitted.V1(emailAddress = emailAddress, userId = userId, id = newId.internal(), timestamp = clock.now(), context = this@InvocationContext.toEventContext())
}