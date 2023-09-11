package org.sollecitom.chassis.example.write_endpoint.domain.user

import org.sollecitom.chassis.ddd.domain.Entity

interface User : Entity {

    suspend fun submitRegistrationRequest(): UserRegistrationEvent

    companion object
}