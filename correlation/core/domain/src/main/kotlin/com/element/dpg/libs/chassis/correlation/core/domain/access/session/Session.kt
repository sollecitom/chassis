package com.element.dpg.libs.chassis.correlation.core.domain.access.session

import org.sollecitom.chassis.core.domain.identity.Id

sealed interface Session {

    val id: Id

    companion object
}