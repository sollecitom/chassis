package com.element.dpg.libs.chassis.kotlin.extensions.time

import kotlinx.datetime.*

fun Clock.localDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime = now().toLocalDateTime(timeZone)

fun Clock.localDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate = localDateTime(timeZone).date

fun Clock.localTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalTime = localDateTime(timeZone).time

val Clock.localDateTime: LocalDateTime get() = localDateTime()

val Clock.localDate: LocalDate get() = localDate()

val Clock.localTime: LocalTime get() = localTime()

fun Clock.Companion.fixed(instant: Instant): Clock = FixedInstantClock(instant)

private class FixedInstantClock(val instant: Instant) : Clock {

    override fun now() = instant
}