package org.sollecitom.chassis.kotlin.extensions.time

import kotlinx.datetime.*
import java.time.temporal.ChronoUnit

fun Instant.truncatedToMilliseconds(): Instant = toJavaInstant().truncatedTo(ChronoUnit.MILLIS).toKotlinInstant()

fun Instant.truncatedToSeconds(): Instant = toJavaInstant().truncatedTo(ChronoUnit.SECONDS).toKotlinInstant()

val Int.years: DatePeriod get() = DatePeriod(years = this)

val Int.months: DatePeriod get() = DatePeriod(months = this)