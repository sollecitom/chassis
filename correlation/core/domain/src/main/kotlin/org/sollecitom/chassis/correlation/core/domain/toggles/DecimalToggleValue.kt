package org.sollecitom.chassis.correlation.core.domain.toggles

import org.sollecitom.chassis.core.domain.identity.Id

data class DecimalToggleValue(override val id: Id, override val value: Double) : ToggleValue<Double> {

    companion object
}