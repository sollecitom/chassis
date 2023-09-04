package org.sollecitom.chassis.correlation.core.domain.toggles

import org.sollecitom.chassis.core.domain.identity.Id

data class EnumToggleValue(override val id: Id, override val value: String) : ToggleValue<String> {

    companion object
}