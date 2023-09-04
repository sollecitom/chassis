package org.sollecitom.chassis.correlation.core.domain.toggles

import org.sollecitom.chassis.core.domain.traits.Identifiable

interface Toggle<VALUE : Any, SERIALIZED_VALUE : Any> : Identifiable {

    operator fun invoke(value: VALUE): ToggleValue<SERIALIZED_VALUE>
}