package org.sollecitom.chassis.correlation.core.domain.toggles.standard.template

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.toggles.EnumToggleValue
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggle
import org.sollecitom.chassis.correlation.core.domain.toggles.ToggleValue
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles

internal class EnumToggle<VALUE : Enum<VALUE>>(override val id: Id, private val deserializeValue: (String) -> VALUE) : Toggle<VALUE, String> {

    override operator fun invoke(toggles: Toggles) = toggles[id]?.let(ToggleValue<*>::value)?.let { it as String }?.let(deserializeValue)

    override fun invoke(value: VALUE) = EnumToggleValue(id = id, value = value.name)
}