package org.sollecitom.chassis.correlation.core.domain.trace

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id

data class InvocationTrace<out ID : Id<ID>>(val id: ID, val createdAt: Instant) {

    companion object
}