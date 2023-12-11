package org.sollecitom.chassis.core.utils

import kotlinx.datetime.Clock
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.sollecitom.chassis.configuration.utils.StandardEnvironment
import org.sollecitom.chassis.configuration.utils.maximumNodesCount
import org.sollecitom.chassis.configuration.utils.nodeId
import org.sollecitom.chassis.configuration.utils.randomSeed
import org.sollecitom.chassis.core.domain.identity.ClusterCoordinates
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import org.sollecitom.chassis.logger.core.loggable.Loggable
import kotlin.random.Random

internal class CoreDataGeneratorProvider(private val environment: Environment, initialisedClusterCoordinates: ClusterCoordinates? = null, initialisedClock: Clock? = null, initialisedRandom: Random? = null) : Loggable(), CoreDataGenerator {

    override val random: Random = initialisedRandom ?: initialiseRandom()
    override val clock: Clock = initialisedClock ?: initialiseClock()
    private val clusterCoordinates = initialisedClusterCoordinates ?: initialiseClusterCoordinates()
    override val newId: UniqueIdFactory by lazy { UniqueIdFactory.invoke(random = random, clock = clock, clusterCoordinates = clusterCoordinates) }

    private fun initialiseRandom(): Random {

        logger.info { "Reading random seed from property ${EnvironmentKey.randomSeed.meta.name}" }
        val seed = EnvironmentKey.randomSeed(environment)
        logger.info { "Initialised random from seed: $seed" }
        return Random(seed)
    }

    private fun initialiseClock(): Clock {

        val clock = Clock.System
        logger.info { "Initialised clock to be the system clock" }
        return clock
    }

    private fun initialiseClusterCoordinates(): ClusterCoordinates {

        logger.info { "Reading nodeID from property ${EnvironmentKey.nodeId.meta.name}" }
        val nodeId = EnvironmentKey.nodeId(environment)
        logger.info { "Reading maximumNodesCount from property ${EnvironmentKey.maximumNodesCount.meta.name}" }
        val maximumNodesCount = EnvironmentKey.maximumNodesCount(environment)
        val clusterCoordinates = ClusterCoordinates(nodeId = nodeId, maximumNodesCount = maximumNodesCount)
        logger.info { "Initialised clusterCoordinates with value: $clusterCoordinates" }
        return clusterCoordinates
    }
}

fun CoreDataGenerator.Companion.provider(environment: Environment = StandardEnvironment(), clusterCoordinates: ClusterCoordinates? = null, clock: Clock? = null, random: Random? = null): CoreDataGenerator = CoreDataGeneratorProvider(environment = environment, initialisedClusterCoordinates = clusterCoordinates, initialisedClock = clock, initialisedRandom = random)