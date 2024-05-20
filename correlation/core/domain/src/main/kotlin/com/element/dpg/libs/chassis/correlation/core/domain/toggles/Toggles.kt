package com.element.dpg.libs.chassis.correlation.core.domain.toggles

import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext

data class Toggles(val values: Set<ToggleValue<*>> = emptySet()) {

    private val byId = values.associateBy(ToggleValue<*>::id)

    operator fun get(toggleId: Id): ToggleValue<*>? = byId[toggleId]

    companion object
}

@Suppress("UNCHECKED_CAST")
operator fun <VALUE : Any> Toggles.get(toggle: ToggleValueExtractor<VALUE>): VALUE? {

    val rawValue = get(toggle.id)?.let(ToggleValue<*>::value)
    return rawValue?.let { it as VALUE }
}

fun <ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access, VALUE : Any> InvocationContext<ACCESS>.withToggle(toggle: Toggle<VALUE, VALUE>, value: VALUE) = copy(toggles = toggles.withToggle(toggle, value))

fun <ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access, VALUE : Enum<VALUE>> InvocationContext<ACCESS>.withToggle(toggle: Toggle<VALUE, *>, value: VALUE) = copy(toggles = toggles.withToggle(toggle, value))

fun <VALUE : Enum<VALUE>> Toggles.withToggle(toggle: Toggle<VALUE, *>, value: VALUE): Toggles = copy(values = values + toggle(value))

fun <VALUE : Any> Toggles.withToggle(toggle: Toggle<VALUE, VALUE>, value: VALUE): Toggles = copy(values = values + toggle(value))