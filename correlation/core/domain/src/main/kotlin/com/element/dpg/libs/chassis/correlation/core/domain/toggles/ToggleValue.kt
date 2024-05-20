package com.element.dpg.libs.chassis.correlation.core.domain.toggles

import com.element.dpg.libs.chassis.core.domain.traits.Identifiable

sealed interface ToggleValue<out VALUE : Any> : Identifiable {

    val value: VALUE

    companion object
}