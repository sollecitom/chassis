package com.element.dpg.libs.chassis.core.domain.traits

import kotlinx.datetime.Instant

interface Timestamped {

    val timestamp: Instant
}