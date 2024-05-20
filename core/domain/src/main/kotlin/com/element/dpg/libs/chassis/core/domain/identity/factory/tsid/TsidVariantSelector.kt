package com.element.dpg.libs.chassis.core.domain.identity.factory.tsid

import com.element.dpg.libs.chassis.core.domain.identity.InstanceInfo
import com.element.dpg.libs.chassis.core.domain.identity.TSID
import com.element.dpg.libs.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import com.element.dpg.libs.chassis.core.domain.naming.Name

interface TsidVariantSelector {

    val default: SortableTimestampedUniqueIdentifierFactory<TSID>

    fun nodeSpecific(instanceInfo: InstanceInfo): SortableTimestampedUniqueIdentifierFactory<TSID>
}

fun TsidVariantSelector.nodeSpecific(nodeId: Int, maximumNodesCount: Int, groupName: Name) = nodeSpecific(InstanceInfo(id = nodeId, maximumInstancesCount = maximumNodesCount, groupName = groupName))