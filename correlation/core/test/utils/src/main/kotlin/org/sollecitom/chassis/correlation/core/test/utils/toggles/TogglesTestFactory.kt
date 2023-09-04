package org.sollecitom.chassis.correlation.core.test.utils.toggles

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.toggles.*

context(WithCoreGenerators)
fun Toggles.Companion.create(values: Set<ToggleValue<*>> = emptySet()) = Toggles(values)

context(WithCoreGenerators)
fun ToggleValue.Companion.boolean(value: Boolean, id: Id = newId.internal()) = BooleanToggleValue(id, value)

context(WithCoreGenerators)
fun ToggleValue.Companion.integer(value: Int, id: Id = newId.internal()) = integer(value.toLong(), id)

context(WithCoreGenerators)
fun ToggleValue.Companion.integer(value: Long, id: Id = newId.internal()) = IntegerToggleValue(id, value)

context(WithCoreGenerators)
fun ToggleValue.Companion.decimal(value: Double, id: Id = newId.internal()) = DecimalToggleValue(id, value)

context(WithCoreGenerators)
fun <VALUE : Enum<VALUE>> ToggleValue.Companion.enum(value: VALUE, id: Id = newId.internal()) = EnumToggleValue(id, value.name)