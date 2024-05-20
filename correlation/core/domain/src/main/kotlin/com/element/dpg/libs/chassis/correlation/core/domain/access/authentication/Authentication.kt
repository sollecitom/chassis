package com.element.dpg.libs.chassis.correlation.core.domain.access.authentication

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id

sealed interface Authentication {

    data class Token(val id: Id, val validFrom: Instant?, val validTo: Instant?) {

        companion object
    }

    companion object
}