package org.sollecitom.chassis.correlation.core.domain.access.authentication

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.StringId

sealed interface Authentication {

    data class Token(val id: StringId, val validFrom: Instant?, val validTo: Instant?) {

        companion object
    }

    companion object
}