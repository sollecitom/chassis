package org.sollecitom.chassis.lens.identity.extensions

import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.BiDiMapping
import org.http4k.lens.StringBiDiMappings
import org.http4k.lens.map
import org.sollecitom.chassis.identity.generator.ulid.ULID

fun <IN : Any> BiDiLensSpec<IN, String>.ulid() = map(StringBiDiMappings.ulid())

fun StringBiDiMappings.ulid() = BiDiMapping(ULID::invoke, ULID::stringValue)