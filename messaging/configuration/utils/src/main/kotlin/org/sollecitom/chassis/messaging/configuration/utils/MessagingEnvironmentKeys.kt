package org.sollecitom.chassis.messaging.configuration.utils

import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.BiDiMapping
import org.http4k.lens.StringBiDiMappings
import org.http4k.lens.map
import org.sollecitom.chassis.messaging.domain.TenantAgnosticTopic
import org.sollecitom.chassis.messaging.domain.Topic
import org.sollecitom.chassis.messaging.domain.parse

fun <IN : Any> BiDiLensSpec<IN, String>.topic() = map(StringBiDiMappings.topic())

fun StringBiDiMappings.topic() = BiDiMapping(Topic::parse) { it.fullName.value }

fun StringBiDiMappings.tenantAgnosticTopic() = BiDiMapping(TenantAgnosticTopic.Companion::parse) { it.name.value }