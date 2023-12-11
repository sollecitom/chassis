package org.sollecitom.chassis.core.utils

import kotlinx.datetime.Clock
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.sollecitom.chassis.configuration.utils.StandardEnvironment
import org.sollecitom.chassis.configuration.utils.maximumNodesCount
import org.sollecitom.chassis.configuration.utils.nodeId
import org.sollecitom.chassis.configuration.utils.randomSeed
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import org.sollecitom.chassis.logger.core.loggable.Loggable
import kotlin.random.Random

internal class CoreDataGeneratorProvider(private val environment: Environment, initialisedNodeId: Int? = null, initialisedMaximumNodesCount: Int? = null, initialisedClock: Clock? = null, initialisedRandom: Random? = null) : Loggable(), CoreDataGenerator {

    override val random: Random = initialisedRandom ?: initialiseRandom()
    override val clock: Clock = initialisedClock ?: initialiseClock()
    private val nodeId = initialisedNodeId ?: initialiseNodeId()
    private val maximumNodesCount = initialisedMaximumNodesCount ?: initialiseMaximumNodesCount()
    override val newId: UniqueIdFactory by lazy { UniqueIdFactory.invoke(random = random, clock = clock, nodeId = nodeId, maximumNodesCount = maximumNodesCount) }

    init {
        require(nodeId >= 0) { "nodeId should be greater than or equal to 0" }
        require(maximumNodesCount >= 256) { "maximumNodesCount should be greater than or equal to 256" }
    }

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

    private fun initialiseNodeId(): Int {

        logger.info { "Reading nodeID from property ${EnvironmentKey.nodeId.meta.name}" }
        val nodeId = EnvironmentKey.nodeId(environment)
        logger.info { "Initialised node ID with value: $nodeId" }
        return nodeId
    }

    private fun initialiseMaximumNodesCount(): Int {

        logger.info { "Reading maximumNodesCount from property ${EnvironmentKey.maximumNodesCount.meta.name}" }
        val maximumNodesCount = EnvironmentKey.maximumNodesCount(environment)
        logger.info { "Initialised maximumNodesCount with value: $maximumNodesCount" }
        return maximumNodesCount
    }
}

fun CoreDataGenerator.Companion.provider(environment: Environment = StandardEnvironment(), nodeId: Int? = null, maximumNodesCount: Int? = null, clock: Clock? = null, random: Random? = null): CoreDataGenerator = CoreDataGeneratorProvider(environment = environment, initialisedNodeId = nodeId, initialisedMaximumNodesCount = maximumNodesCount, initialisedClock = clock, initialisedRandom = random)