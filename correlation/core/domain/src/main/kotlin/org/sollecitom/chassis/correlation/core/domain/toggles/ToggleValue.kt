package org.sollecitom.chassis.correlation.core.domain.toggles

import org.sollecitom.chassis.core.domain.traits.Identifiable

sealed interface ToggleValue<out VALUE : Any> : Identifiable {

    val value: VALUE

    companion object
}