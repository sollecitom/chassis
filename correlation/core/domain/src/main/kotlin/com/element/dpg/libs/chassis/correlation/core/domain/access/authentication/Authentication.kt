package com.element.dpg.libs.chassis.correlation.core.domain.access.authentication

import com.element.dpg.libs.chassis.core.domain.identity.Id
import kotlinx.datetime.Instant

sealed interface Authentication {

    data class Token(val id: Id, val validFrom: Instant?, val validTo: Instant?) {

        companion object
    }

    companion object
}