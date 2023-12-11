package org.sollecitom.chassis.core.domain.identity.factory.tsid

import kotlinx.datetime.Clock
import org.sollecitom.chassis.core.domain.identity.TSID
import org.sollecitom.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import kotlin.random.Random

internal class TsidVariantSelectorAdapter(private val random: Random, private val clock: Clock) : TsidVariantSelector {

    override val default: SortableTimestampedUniqueIdentifierFactory<TSID> = DefaultTsidFactoryAdapter(random, clock)

    override fun nodeSpecific(nodeId: Int, nodeBits: Int): SortableTimestampedUniqueIdentifierFactory<TSID> = NodeSpecificTsidFactoryAdapter(nodeId, nodeBits, random, clock)
}