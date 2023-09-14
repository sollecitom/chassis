package org.sollecitom.chassis.example.write_endpoint.domain.user

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.toEventContext
import org.sollecitom.chassis.example.write_endpoint.domain.user.User
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserEvent
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRegistrationRequestWasSubmitted

context(CoreDataGenerator)
internal class UnregisteredUser(override val id: Id, private val emailAddress: EmailAddress, private val publish: suspend (UserEvent) -> Unit) : User {

    context(InvocationContext<*>)
    override suspend fun submitRegistrationRequest(): UserRegistrationRequestWasSubmitted.V1 {

        val event = registrationRequestWasSubmitted()
        publish(event)
        return event
    }

    context(InvocationContext<*>)
    private fun registrationRequestWasSubmitted(): UserRegistrationRequestWasSubmitted.V1 {

        val now = clock.now()
        return UserRegistrationRequestWasSubmitted.V1(emailAddress = emailAddress, userId = id, id = newId.internal(now), timestamp = now, context = this@InvocationContext.toEventContext())
    }
}