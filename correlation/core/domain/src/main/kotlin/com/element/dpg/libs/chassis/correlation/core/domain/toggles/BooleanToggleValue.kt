package com.element.dpg.libs.chassis.correlation.core.domain.toggles

import com.element.dpg.libs.chassis.core.domain.identity.Id

data class BooleanToggleValue(override val id: Id, override val value: Boolean) : ToggleValue<Boolean> {

    companion object
}