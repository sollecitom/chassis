package org.sollecitom.chassis.correlation.core.domain.trace

import org.sollecitom.chassis.core.domain.identity.Id

data class Trace<out ID : Id<ID>>(val invocation: InvocationTrace<ID>, val parent: ParentTrace<ID>?, val originating: OriginatingTrace?) {

    // TODO add forking
}