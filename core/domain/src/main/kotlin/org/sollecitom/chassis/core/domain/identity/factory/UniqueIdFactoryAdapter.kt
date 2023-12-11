package org.sollecitom.chassis.core.domain.identity.factory

import kotlinx.datetime.Clock
import org.sollecitom.chassis.core.domain.identity.factory.ksuid.KsuidVariantSelectorAdapter
import org.sollecitom.chassis.core.domain.identity.factory.string.StringFactoryAdapter
import org.sollecitom.chassis.core.domain.identity.factory.tsid.TsidVariantSelectorAdapter
import org.sollecitom.chassis.core.domain.identity.factory.ulid.UlidVariantSelectorAdapter
import kotlin.random.Random

private class UniqueIdFactoryAdapter(random: Random = Random, clock: Clock = Clock.System, private val nodeId: Int, private val maximumNodesCount: Int) : UniqueIdFactory {

    init {
        require(nodeId >= 0) { "Node ID must be greater than or equal to 0" }
        require(maximumNodesCount >= 2) { "The maximum nodes count must be greater than or equal to 1" }
        require(nodeId < maximumNodesCount) { "Node ID must be less than the maximum nodes count" }
    }

    override val ulid by lazy { UlidVariantSelectorAdapter(random, clock) }
    override val ksuid by lazy { KsuidVariantSelectorAdapter(random, clock) }
    override val tsid by lazy { TsidVariantSelectorAdapter(random, clock) }
    override val internal get() = ksuid.monotonic
    override val forEntities by lazy { tsid.nodeSpecific(nodeId, maximumNodesCount) }
    override val external by lazy { StringFactoryAdapter(random) { ulid.monotonic().stringValue } }
}

operator fun UniqueIdFactory.Companion.invoke(random: Random = Random, clock: Clock = Clock.System, nodeId: Int, maximumNodesCount: Int): UniqueIdFactory = UniqueIdFactoryAdapter(random, clock, nodeId, maximumNodesCount)