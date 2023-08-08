package org.sollecitom.chassis.example.service.endpoint.write.domain.user

import org.sollecitom.chassis.core.domain.email.EmailAddress

interface UserRepository {

    suspend fun withEmailAddress(emailAddress: EmailAddress): User

    companion object
}