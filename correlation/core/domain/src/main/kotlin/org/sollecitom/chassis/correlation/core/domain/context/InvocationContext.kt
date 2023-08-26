package org.sollecitom.chassis.correlation.core.domain.context

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.trace.Trace

data class InvocationContext<ACCESS : Access<ACCESS_ID, AUTHENTICATION>, ACCESS_ID : Id<ACCESS_ID>, AUTHENTICATION : Authentication, TRACE_ID : SortableTimestampedUniqueIdentifier<TRACE_ID>>(val access: ACCESS, val trace: Trace<TRACE_ID>) {

    companion object
}