package com.element.dpg.libs.chassis.correlation.core.domain.toggles

import com.element.dpg.libs.chassis.core.domain.identity.Id

data class DecimalToggleValue(override val id: Id, override val value: Double) : ToggleValue<Double> {

    companion object
}