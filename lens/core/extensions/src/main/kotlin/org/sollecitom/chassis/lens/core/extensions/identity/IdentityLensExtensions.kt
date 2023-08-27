package org.sollecitom.chassis.lens.core.extensions.identity

import org.http4k.lens.*
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.lens.core.extensions.map

fun Path.ulid() = map(StringBiDiMappings.ulid())

fun <IN : Any> BiDiLensSpec<IN, String>.ulid() = map(StringBiDiMappings.ulid())

fun StringBiDiMappings.ulid() = BiDiMapping(ULID::invoke, ULID::stringValue)