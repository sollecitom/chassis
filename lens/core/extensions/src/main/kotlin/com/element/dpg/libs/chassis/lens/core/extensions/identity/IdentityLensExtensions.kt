package com.element.dpg.libs.chassis.lens.core.extensions.identity

import org.http4k.lens.*
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.ULID
import org.sollecitom.chassis.core.domain.identity.fromString

fun Path.ulid() = map(StringBiDiMappings.ulid())
fun <IN : Any> BiDiLensSpec<IN, String>.ulid() = map(StringBiDiMappings.ulid())
fun StringBiDiMappings.ulid() = BiDiMapping(ULID::invoke, ULID::stringValue)

fun Path.id() = map(StringBiDiMappings.id())
fun <IN : Any> BiDiLensSpec<IN, String>.id() = map(StringBiDiMappings.id())
fun StringBiDiMappings.id() = BiDiMapping(Id.Companion::fromString, Id::stringValue)