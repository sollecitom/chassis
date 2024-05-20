package com.element.dpg.libs.chassis.correlation.core.domain.toggles

import org.sollecitom.chassis.core.domain.identity.Id

data class IntegerToggleValue(override val id: Id, override val value: Long) : ToggleValue<Long> {

    companion object
}