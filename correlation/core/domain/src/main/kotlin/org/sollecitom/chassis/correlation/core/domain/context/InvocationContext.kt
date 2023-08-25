package org.sollecitom.chassis.correlation.core.domain.context

import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.correlation.core.domain.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.origin.Origin
import org.sollecitom.chassis.correlation.core.domain.trace.Trace

// TODO remove origin and add Access instead
data class InvocationContext<ID : SortableTimestampedUniqueIdentifier<ID>>(val trace: Trace<ID>, val origin: Origin, private val authorization: AuthorizationPrincipal) : AuthorizationPrincipal by authorization {

    companion object
}