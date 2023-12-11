package org.sollecitom.chassis.core.domain.identity.factory.tsid

import kotlinx.datetime.Clock
import org.sollecitom.chassis.core.domain.identity.ClusterCoordinates
import org.sollecitom.chassis.kotlin.extensions.bytes.requiredBits
import kotlin.random.Random

internal class TsidVariantSelectorAdapter(private val random: Random, private val clock: Clock) : TsidVariantSelector {

    override val default = DefaultTsidFactoryAdapter(random, clock)

    override fun nodeSpecific(clusterCoordinates: ClusterCoordinates) = NodeSpecificTsidFactoryAdapter(clusterCoordinates.nodeId, clusterCoordinates.maximumNodesCount.requiredBits, random, clock)
}