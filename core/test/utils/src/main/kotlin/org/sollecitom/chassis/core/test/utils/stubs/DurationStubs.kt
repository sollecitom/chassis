package org.sollecitom.chassis.core.test.utils.stubs

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import kotlin.time.Duration

context(CoreDataGenerator)
val Duration.ago: Instant
    get() = clock.now() - this

context(CoreDataGenerator)
val Duration.fromNow: Instant
    get() = clock.now() + this