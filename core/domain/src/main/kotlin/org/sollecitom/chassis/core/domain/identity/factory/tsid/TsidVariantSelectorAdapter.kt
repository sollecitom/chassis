package org.sollecitom.chassis.core.domain.identity.factory.tsid

import kotlinx.datetime.Clock
import org.sollecitom.chassis.kotlin.extensions.number.roundToCeil
import kotlin.math.log2
import kotlin.random.Random

internal class TsidVariantSelectorAdapter(private val random: Random, private val clock: Clock) : TsidVariantSelector {

    override val default = DefaultTsidFactoryAdapter(random, clock)

    override fun nodeSpecific(nodeId: Int, maximumNumberOfNodes: Int) = NodeSpecificTsidFactoryAdapter(nodeId, bitsFor(maximumNumberOfNodes), random, clock)

    private fun bitsFor(maximumNumberOfNodes: Int): Int = log2(maximumNumberOfNodes.toDouble()).roundToCeil()
}