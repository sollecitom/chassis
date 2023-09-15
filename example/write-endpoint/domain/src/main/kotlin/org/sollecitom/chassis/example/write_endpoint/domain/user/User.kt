package org.sollecitom.chassis.example.write_endpoint.domain.user

import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.Entity
import org.sollecitom.chassis.ddd.domain.Event

interface User : Entity {

    context(InvocationContext<*>)
    suspend fun submitRegistrationRequest(): Published<UserRegistrationEvent>

    companion object
}

// TODO move
data class Published<out EVENT : Event>(val event: EVENT, val wasPersisted: Deferred<Unit>)