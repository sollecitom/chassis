package org.sollecitom.chassis.example.write_endpoint.domain.user

import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.Entity
import org.sollecitom.chassis.example.event.domain.Published
import org.sollecitom.chassis.example.event.domain.UserRegistrationEvent

interface User : Entity {

    context(InvocationContext<*>)
    suspend fun submitRegistrationRequest(): Published<UserRegistrationEvent>

    companion object
}