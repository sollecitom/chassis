package org.sollecitom.chassis.kotlin.extensions.time

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import java.time.temporal.ChronoUnit

fun Instant.truncatedToMilliseconds(): Instant = toJavaInstant().truncatedTo(ChronoUnit.MILLIS).toKotlinInstant()

fun Instant.truncatedToSeconds(): Instant = toJavaInstant().truncatedTo(ChronoUnit.SECONDS).toKotlinInstant()

val Int.years: DatePeriod get() = DatePeriod(years = this)

val Int.months: DatePeriod get() = DatePeriod(months = this)