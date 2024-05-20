package com.element.dpg.libs.chassis.correlation.core.domain.trace

import org.sollecitom.chassis.core.domain.identity.Id

data class ExternalInvocationTrace(val invocationId: Id, val actionId: Id) {

    companion object
}