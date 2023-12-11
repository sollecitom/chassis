package org.sollecitom.chassis.core.domain.identity.factory.ulid

import kotlinx.datetime.Clock
import org.sollecitom.chassis.core.domain.identity.ULID
import org.sollecitom.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import kotlin.random.Random

internal class UlidVariantSelectorAdapter(random: Random, clock: Clock) : UlidVariantSelector {

    override val monotonic: SortableTimestampedUniqueIdentifierFactory<ULID> = MonotonicUlidFactoryAdapter(random, clock)
}