package org.sollecitom.chassis.core.domain.identity.factory.tsid

import org.sollecitom.chassis.core.domain.identity.TSID
import org.sollecitom.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory

interface TsidVariantSelector {

    val default: SortableTimestampedUniqueIdentifierFactory<TSID>

    fun nodeSpecific(nodeId: Int, nodeBits: Int): SortableTimestampedUniqueIdentifierFactory<TSID>
}