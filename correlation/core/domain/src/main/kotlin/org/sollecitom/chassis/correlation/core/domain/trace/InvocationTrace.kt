package org.sollecitom.chassis.correlation.core.domain.trace

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier

data class InvocationTrace<ID : SortableTimestampedUniqueIdentifier<ID>>(val id: ID, val createdAt: Instant) {

    companion object
}