package com.element.dpg.libs.chassis.core.test.utils.stubs

import com.element.dpg.libs.chassis.core.utils.TimeGenerator
import kotlinx.datetime.Instant
import kotlin.time.Duration

context(TimeGenerator)
val Duration.ago: Instant
    get() = clock.now() - this

context(TimeGenerator)
val Duration.fromNow: Instant
    get() = clock.now() + this