package org.sollecitom.chassis.core.domain.identity.factory.tsid

import org.sollecitom.chassis.core.domain.identity.ClusterCoordinates
import org.sollecitom.chassis.core.domain.identity.TSID
import org.sollecitom.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory

interface TsidVariantSelector {

    val default: SortableTimestampedUniqueIdentifierFactory<TSID>

    fun nodeSpecific(clusterCoordinates: ClusterCoordinates): SortableTimestampedUniqueIdentifierFactory<TSID>
}

fun TsidVariantSelector.nodeSpecific(nodeId: Int, maximumNodesCount: Int) = nodeSpecific(ClusterCoordinates(nodeId, maximumNodesCount))