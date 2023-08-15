package org.sollecitom.chassis.lens.core.extensions

import org.http4k.lens.BiDiLens
import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.BiDiMapping
import org.http4k.lens.BiDiPathLensSpec

internal fun <IN : Any, OUT> BiDiLensSpec<IN, OUT>.requiredWithSameMetaAs(other: BiDiLens<*, *>) = required(other.meta.name, other.meta.description)

internal fun <IN : Any, OUT> BiDiLensSpec<IN, OUT>.optionalWithSameMetaAs(other: BiDiLens<*, *>) = optional(other.meta.name, other.meta.description)

internal fun <IN : Any, OUT> BiDiLensSpec<IN, OUT>.defaultedWithSameMetaAs(defaultValue: OUT, other: BiDiLens<*, *>) = defaulted(other.meta.name, defaultValue, other.meta.description)

internal fun <IN, NEXT> BiDiPathLensSpec<IN>.map(mapping: BiDiMapping<IN, NEXT>) = map(mapping::invoke, mapping::invoke)