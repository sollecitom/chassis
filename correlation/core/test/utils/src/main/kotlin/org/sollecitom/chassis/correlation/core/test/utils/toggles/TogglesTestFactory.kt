package org.sollecitom.chassis.correlation.core.test.utils.toggles

import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.toggles.*

fun Toggles.Companion.create(values: Set<ToggleValue<*>> = emptySet()) = Toggles(values)

context(UniqueIdGenerator)
fun ToggleValue.Companion.boolean(value: Boolean, id: Id = newId.internal()) = BooleanToggleValue(id, value)

context(UniqueIdGenerator)
fun ToggleValue.Companion.integer(value: Int, id: Id = newId.internal()) = integer(value.toLong(), id)

context(UniqueIdGenerator)
fun ToggleValue.Companion.integer(value: Long, id: Id = newId.internal()) = IntegerToggleValue(id, value)

context(UniqueIdGenerator)
fun ToggleValue.Companion.decimal(value: Double, id: Id = newId.internal()) = DecimalToggleValue(id, value)

context(UniqueIdGenerator)
fun <VALUE : Enum<VALUE>> ToggleValue.Companion.enum(value: VALUE, id: Id = newId.internal()) = EnumToggleValue(id, value.name)