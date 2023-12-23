package org.sollecitom.chassis.core.domain.identity.factory

import kotlinx.datetime.Clock
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.core.domain.identity.factory.ksuid.KsuidVariantSelectorAdapter
import org.sollecitom.chassis.core.domain.identity.factory.string.StringFactoryAdapter
import org.sollecitom.chassis.core.domain.identity.factory.tsid.TsidVariantSelectorAdapter
import org.sollecitom.chassis.core.domain.identity.factory.ulid.UlidVariantSelectorAdapter
import kotlin.random.Random

private class UniqueIdFactoryAdapter(random: Random = Random, clock: Clock = Clock.System, instanceInfo: InstanceInfo) : UniqueIdFactory {

    override val ulid by lazy { UlidVariantSelectorAdapter(random, clock) }
    override val ksuid by lazy { KsuidVariantSelectorAdapter(random, clock) }
    override val tsid by lazy { TsidVariantSelectorAdapter(random, clock) }
    override val internal get() = ksuid.monotonic
    override val forEntities by lazy { tsid.nodeSpecific(instanceInfo) }
    override val external by lazy { StringFactoryAdapter(random) { ulid.monotonic().stringValue } }
}

operator fun UniqueIdFactory.Companion.invoke(random: Random = Random, clock: Clock = Clock.System, instanceInfo: InstanceInfo): UniqueIdFactory = UniqueIdFactoryAdapter(random, clock, instanceInfo)