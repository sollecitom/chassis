package org.sollecitom.chassis.kotlin.extensions.time

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import java.time.temporal.ChronoUnit

fun Clock.Companion.fixed(instant: Instant): Clock = FixedInstantClock(instant)

fun Instant.truncatedToMilliseconds(): Instant = toJavaInstant().truncatedTo(ChronoUnit.MILLIS).toKotlinInstant()

private class FixedInstantClock(val instant: Instant) : Clock {

    override fun now() = instant
}