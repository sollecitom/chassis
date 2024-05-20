package com.element.dpg.libs.chassis.lens.core.extensions.naming

import org.http4k.lens.*
import org.sollecitom.chassis.core.domain.naming.Name

fun Path.name() = map(StringBiDiMappings.name())

fun <IN : Any> BiDiLensSpec<IN, String>.name() = map(StringBiDiMappings.name())

fun StringBiDiMappings.name() = BiDiMapping(::Name, Name::value)