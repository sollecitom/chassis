package org.sollecitom.chassis.core.test.utils.stubs

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.utils.TimeGenerator
import kotlin.time.Duration

context(TimeGenerator)
val Duration.ago: Instant
    get() = clock.now() - this

context(TimeGenerator)
val Duration.fromNow: Instant
    get() = clock.now() + this