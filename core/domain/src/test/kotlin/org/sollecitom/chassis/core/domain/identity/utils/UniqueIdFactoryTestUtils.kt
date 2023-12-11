package org.sollecitom.chassis.core.domain.identity.utils

import kotlinx.datetime.Clock
import org.sollecitom.chassis.core.domain.identity.ClusterCoordinates
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import kotlin.random.Random

operator fun UniqueIdFactory.Companion.invoke(random: Random = Random, clock: Clock = Clock.System, clusterCoordinates: ClusterCoordinates = testClusterCoordinates): UniqueIdFactory = UniqueIdFactory.invoke(random, clock, clusterCoordinates)

private val testClusterCoordinates = ClusterCoordinates(nodeId = 0, maximumNodesCount = 256)