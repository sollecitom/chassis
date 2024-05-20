package com.element.dpg.libs.chassis.messaging.configuration.utils

import com.element.dpg.libs.chassis.messaging.domain.TenantAgnosticTopic
import com.element.dpg.libs.chassis.messaging.domain.Topic
import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.BiDiMapping
import org.http4k.lens.StringBiDiMappings
import org.http4k.lens.map

fun <IN : Any> BiDiLensSpec<IN, String>.topic() = map(StringBiDiMappings.topic())

fun StringBiDiMappings.topic() = BiDiMapping(Topic::parse) { it.fullName.value }

fun <IN : Any> BiDiLensSpec<IN, String>.tenantAgnosticTopic() = map(StringBiDiMappings.tenantAgnosticTopic())

fun StringBiDiMappings.tenantAgnosticTopic() = BiDiMapping(TenantAgnosticTopic.Companion::parse) { it.name.value }