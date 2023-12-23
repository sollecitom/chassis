package org.sollecitom.chassis.pulsar.utils

import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.BiDiMapping
import org.http4k.lens.StringBiDiMappings
import org.http4k.lens.map

// TODO remove?
fun <IN : Any> BiDiLensSpec<IN, String>.pulsarTopic() = map(StringBiDiMappings.pulsarTopic())

fun StringBiDiMappings.pulsarTopic() = BiDiMapping(PulsarTopic::parse) { it.fullName.value }