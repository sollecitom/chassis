package org.sollecitom.chassis.correlation.core.domain.context

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.trace.Trace

interface InvocationContext<out ID : Id<ID>> {

    val trace: Trace<ID>

    companion object
}