package org.sollecitom.chassis.correlation.core.domain.idempotency

import org.sollecitom.chassis.core.domain.naming.Name

data class IdempotencyContext(val namespace: Name?, val key: Name) {

    fun id(separator: String = DEFAULT_IDEMPOTENCY_ID_SEGMENTS_SEPARATOR): Name = namespace?.let { "${it.value}${separator}${key.value}".let(::Name) } ?: key

    companion object {

        const val DEFAULT_IDEMPOTENCY_ID_SEGMENTS_SEPARATOR = "-"
    }
}