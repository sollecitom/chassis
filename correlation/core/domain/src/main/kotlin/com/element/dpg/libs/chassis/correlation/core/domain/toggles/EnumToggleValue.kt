package com.element.dpg.libs.chassis.correlation.core.domain.toggles

import com.element.dpg.libs.chassis.core.domain.identity.Id

data class EnumToggleValue(override val id: Id, override val value: String) : ToggleValue<String> {

    companion object
}