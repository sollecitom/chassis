package org.sollecitom.chassis.correlation.core.domain.context

import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.correlation.core.domain.origin.Origin
import org.sollecitom.chassis.correlation.core.domain.trace.Trace

data class InvocationContext<ID : SortableTimestampedUniqueIdentifier<ID>>(val trace: Trace<ID>, val origin: Origin) {

    companion object
}