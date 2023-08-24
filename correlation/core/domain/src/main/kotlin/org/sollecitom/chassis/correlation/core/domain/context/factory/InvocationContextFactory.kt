package org.sollecitom.chassis.correlation.core.domain.context.factory

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.Trace

interface InvocationContextFactory {

    operator fun <ID : Id<ID>> invoke(trace: Trace<ID>): InvocationContext<ID>
}

