package org.sollecitom.chassis.core.test.utils

import org.sollecitom.chassis.core.domain.identity.ClusterCoordinates
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.provider

val CoreDataGenerator.Companion.testProvider: CoreDataGenerator by lazy { CoreDataGenerator.provider(clusterCoordinates = testClusterCoordinates) }

private val testClusterCoordinates = ClusterCoordinates(nodeId = 0, maximumNodesCount = 256)