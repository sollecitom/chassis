package org.sollecitom.chassis.correlation.core.domain.trace

import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier

data class Trace<ID : SortableTimestampedUniqueIdentifier<ID>>(val invocation: InvocationTrace<ID>, val parent: ParentTrace<ID>?, val originating: OriginatingTrace?) {

    // TODO add forking

    companion object
}