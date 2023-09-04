package org.sollecitom.chassis.correlation.core.domain.toggles

data class Toggles(val values: Set<ToggleValue<*>> = emptySet()) {

    companion object
}

fun <VALUE : Enum<VALUE>> Toggles.withToggle(toggle: Toggle<VALUE, *>, value: VALUE): Toggles = copy(values = values + toggle(value))

fun <VALUE : Any> Toggles.withToggle(toggle: Toggle<VALUE, VALUE>, value: VALUE): Toggles = copy(values = values + toggle(value))