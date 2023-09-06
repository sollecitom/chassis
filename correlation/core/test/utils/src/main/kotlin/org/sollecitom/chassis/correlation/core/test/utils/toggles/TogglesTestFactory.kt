package org.sollecitom.chassis.correlation.core.test.utils.toggles

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.toggles.*

context(CoreDataGenerator)
fun Toggles.Companion.create(values: Set<ToggleValue<*>> = emptySet()) = Toggles(values)

context(CoreDataGenerator)
fun ToggleValue.Companion.boolean(value: Boolean, id: Id = newId.internal()) = BooleanToggleValue(id, value)

context(CoreDataGenerator)
fun ToggleValue.Companion.integer(value: Int, id: Id = newId.internal()) = integer(value.toLong(), id)

context(CoreDataGenerator)
fun ToggleValue.Companion.integer(value: Long, id: Id = newId.internal()) = IntegerToggleValue(id, value)

context(CoreDataGenerator)
fun ToggleValue.Companion.decimal(value: Double, id: Id = newId.internal()) = DecimalToggleValue(id, value)

context(CoreDataGenerator)
fun <VALUE : Enum<VALUE>> ToggleValue.Companion.enum(value: VALUE, id: Id = newId.internal()) = EnumToggleValue(id, value.name)