package org.sollecitom.chassis.core.domain.identity

data class ClusterCoordinates(val nodeId: Int, val maximumNodesCount: Int) {

    init {
        require(nodeId >= 0) { "Node ID must be greater than or equal to 0" }
        require(maximumNodesCount >= MINIMUM_CLUSTER_SIZE) { "The maximum nodes count must be greater than or equal to $MINIMUM_CLUSTER_SIZE" }
        require(nodeId < maximumNodesCount) { "Node ID must be less than the maximum nodes count" }
    }

    companion object {
        const val MINIMUM_CLUSTER_SIZE = 256
    }
}