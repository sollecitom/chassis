package org.sollecitom.chassis.core.utils

import kotlinx.datetime.Clock
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.sollecitom.chassis.configuration.utils.StandardEnvironment
import org.sollecitom.chassis.configuration.utils.instanceInfo
import org.sollecitom.chassis.configuration.utils.randomSeed
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import org.sollecitom.chassis.kotlin.extensions.number.toByteArray
import org.sollecitom.chassis.logger.core.loggable.Loggable
import java.security.SecureRandom
import kotlin.random.Random

internal class CoreDataGeneratorProvider(private val environment: Environment, initialisedInstanceInfo: InstanceInfo? = null, initialisedClock: Clock? = null, initialisedRandom: Random? = null, seed: ByteArray? = null) : Loggable(), CoreDataGenerator {

    override val random: Random = initialisedRandom ?: initialiseRandom()
    override val secureRandom: SecureRandom = SecureRandom(seed ?: initialiseSecureRandomSeed())
    override val clock: Clock = initialisedClock ?: initialiseClock()
    private val clusterCoordinates = initialisedInstanceInfo ?: initialiseClusterCoordinates()
    override val newId: UniqueIdFactory by lazy { UniqueIdFactory.invoke(random = random, clock = clock, instanceInfo = clusterCoordinates) }

    private fun initialiseRandom(): Random {

        logger.info { "Reading random seed from property ${EnvironmentKey.randomSeed.meta.name}" }
        val seed = EnvironmentKey.randomSeed(environment)
        logger.info { "Initialised random from seed: $seed" }
        return Random(seed)
    }

    private fun initialiseSecureRandomSeed(): ByteArray {

        logger.info { "Reading random seed from property ${EnvironmentKey.randomSeed.meta.name}" }
        val seed = EnvironmentKey.randomSeed(environment)
        logger.info { "Initialised random from seed: $seed" }
        return seed.toByteArray()
    }

    private fun initialiseClock(): Clock {

        val clock = Clock.System
        logger.info { "Initialised clock to be the system clock" }
        return clock
    }

    private fun initialiseClusterCoordinates(): InstanceInfo {

        val instanceInfo = environment.instanceInfo()
        logger.info { "Initialised clusterCoordinates with value: $instanceInfo" }
        return instanceInfo
    }
}

fun CoreDataGenerator.Companion.provider(environment: Environment = StandardEnvironment(), instanceInfo: InstanceInfo? = null, clock: Clock? = null, random: Random? = null, seed: ByteArray? = null): CoreDataGenerator = CoreDataGeneratorProvider(environment = environment, initialisedInstanceInfo = instanceInfo, initialisedClock = clock, initialisedRandom = random, seed = seed)