package com.element.dpg.libs.chassis.lens.core.extensions.time

import org.http4k.cloudnative.env.Environment
import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.duration
import kotlin.time.Duration
import kotlin.time.toJavaDuration
import kotlin.time.toKotlinDuration
import java.time.Duration as JavaDuration

fun BiDiLensSpec<Environment, String>.kotlinDuration() = duration().map(JavaDuration::toKotlinDuration, Duration::toJavaDuration)