package com.element.dpg.libs.chassis.correlation.core.domain.trace

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id

data class InvocationTrace(val id: Id, val createdAt: Instant) {

    companion object
}