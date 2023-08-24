package org.sollecitom.chassis.correlation.core.domain.trace

import org.sollecitom.chassis.core.domain.identity.StringId

data class OriginatingTrace(val invocationId: StringId, val actionId: StringId)