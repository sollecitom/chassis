package org.sollecitom.chassis.correlation.core.domain.trace

import org.sollecitom.chassis.core.domain.identity.StringId

data class ExternalInvocationTrace(val invocationId: StringId, val actionId: StringId) {

    companion object
}