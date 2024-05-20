package com.element.dpg.libs.chassis.correlation.core.domain.trace

import com.element.dpg.libs.chassis.core.domain.identity.Id
import kotlinx.datetime.Instant

data class InvocationTrace(val id: Id, val createdAt: Instant) {

    companion object
}