package org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.memory

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted

context(CoreDataGenerator)
internal class UnregisteredUser(override val id: Id, private val emailAddress: EmailAddress) : User {

    override suspend fun submitRegistrationRequest() = registrationRequestWasSubmitted()

    private fun registrationRequestWasSubmitted(): UserRegistrationRequestWasSubmitted.V1 {

        val now = clock.now()
        return UserRegistrationRequestWasSubmitted.V1(emailAddress = emailAddress, userId = id, id = newId.internal(now), timestamp = now)
    }
}