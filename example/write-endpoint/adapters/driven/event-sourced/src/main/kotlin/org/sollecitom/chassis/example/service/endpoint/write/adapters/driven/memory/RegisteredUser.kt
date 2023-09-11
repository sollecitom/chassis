package org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.memory

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserEvent
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted

// TODO replace with a single type that uses the event history instead
context(CoreDataGenerator)
internal class RegisteredUser(private val pastUserRegistrationRequest: UserRegistrationRequestWasSubmitted, private val publish: suspend (UserEvent) -> Unit) : User {

    override val id: Id get() = pastUserRegistrationRequest.userId

    override suspend fun submitRegistrationRequest(): UserRegistrationRequestWasAlreadySubmitted.V1 {

        val event = pastUserRegistrationRequest.alreadySubmitted()
        publish(event)
        return event
    }

    private fun UserRegistrationRequestWasSubmitted.alreadySubmitted() = UserRegistrationRequestWasAlreadySubmitted.V1(emailAddress = emailAddress, userId = userId, id = newId.internal(), timestamp = clock.now())
}