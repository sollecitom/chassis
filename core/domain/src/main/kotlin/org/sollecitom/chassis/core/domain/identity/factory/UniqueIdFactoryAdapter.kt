package org.sollecitom.chassis.core.domain.identity.factory

import kotlinx.datetime.Clock
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.factory.ksuid.KsuidVariantSelectorAdapter
import org.sollecitom.chassis.core.domain.identity.factory.string.StringFactoryAdapter
import org.sollecitom.chassis.core.domain.identity.factory.tsid.TsidVariantSelectorAdapter
import org.sollecitom.chassis.core.domain.identity.factory.ulid.UlidVariantSelectorAdapter
import kotlin.random.Random

private class UniqueIdFactoryAdapter(random: Random = Random, clock: Clock = Clock.System) : UniqueIdFactory {

    override val ulid = UlidVariantSelectorAdapter(random, clock)
    override val ksuid = KsuidVariantSelectorAdapter(random, clock)
    override val tsid = TsidVariantSelectorAdapter(random, clock)
    override val internal: SortableTimestampedUniqueIdentifierFactory<*> get() = ksuid.monotonic
    override val external: UniqueIdentifierFactory<Id> = StringFactoryAdapter(random) { internal().stringValue }
}

operator fun UniqueIdFactory.Companion.invoke(random: Random = Random, clock: Clock = Clock.System): UniqueIdFactory = UniqueIdFactoryAdapter(random, clock)

