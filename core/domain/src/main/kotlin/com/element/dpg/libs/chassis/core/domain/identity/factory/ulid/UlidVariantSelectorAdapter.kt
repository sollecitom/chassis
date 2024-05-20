package com.element.dpg.libs.chassis.core.domain.identity.factory.ulid

import com.element.dpg.libs.chassis.core.domain.identity.ULID
import com.element.dpg.libs.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import kotlinx.datetime.Clock
import kotlin.random.Random

internal class UlidVariantSelectorAdapter(random: Random, clock: Clock) : UlidVariantSelector {

    override val monotonic: SortableTimestampedUniqueIdentifierFactory<ULID> = MonotonicUlidFactoryAdapter(random, clock)
}