package org.sollecitom.chassis.example.write_endpoint.domain.user

import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.Entity

interface User : Entity {

    context(InvocationContext<*>)
    suspend fun submitRegistrationRequest(): UserRegistrationEvent

    companion object
}