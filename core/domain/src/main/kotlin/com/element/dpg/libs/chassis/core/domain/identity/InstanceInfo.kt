package com.element.dpg.libs.chassis.core.domain.identity

import com.element.dpg.libs.chassis.core.domain.naming.Name

data class InstanceInfo(val id: Int, val maximumInstancesCount: Int, val groupName: Name) {

    init {
        require(id >= 0) { "Instance ID must be greater than or equal to 0" }
        require(maximumInstancesCount >= MINIMUM_CLUSTER_SIZE) { "The maximum instances count must be greater than or equal to $MINIMUM_CLUSTER_SIZE" }
        require(id < maximumInstancesCount) { "Node ID must be less than the maximum instances count" }
    }

    companion object {
        const val MINIMUM_CLUSTER_SIZE = 256
    }
}