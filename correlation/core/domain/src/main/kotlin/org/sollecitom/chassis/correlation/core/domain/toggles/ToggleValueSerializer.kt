package org.sollecitom.chassis.correlation.core.domain.toggles

interface ToggleValueSerializer<in VALUE : Any, out SERIALIZED_VALUE : Any> {

    operator fun invoke(value: VALUE): ToggleValue<SERIALIZED_VALUE>
}