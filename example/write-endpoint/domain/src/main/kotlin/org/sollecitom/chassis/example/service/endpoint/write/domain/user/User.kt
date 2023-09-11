package org.sollecitom.chassis.example.service.endpoint.write.domain.user

import org.sollecitom.chassis.ddd.domain.Entity

interface User : Entity {

    suspend fun submitRegistrationRequest(): UserRegistrationEvent

    companion object
}