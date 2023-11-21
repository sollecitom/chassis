package org.sollecitom.chassis.kotlin.extensions.duration

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

val Int.weeks: Duration get() = (this * 7).days