package org.sollecitom.chassis.correlation.core.domain.context.factory

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.Trace

internal data class StandardInvocationContext<out ID : Id<ID>>(override val trace: Trace<ID>) : InvocationContext<ID>