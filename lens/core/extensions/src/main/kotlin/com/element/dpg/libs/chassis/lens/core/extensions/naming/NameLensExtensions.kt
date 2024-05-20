package com.element.dpg.libs.chassis.lens.core.extensions.naming

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.lens.core.extensions.map
import org.http4k.lens.*

fun Path.name() = map(StringBiDiMappings.name())

fun <IN : Any> BiDiLensSpec<IN, String>.name() = map(StringBiDiMappings.name())

fun StringBiDiMappings.name() = BiDiMapping(::Name, Name::value)