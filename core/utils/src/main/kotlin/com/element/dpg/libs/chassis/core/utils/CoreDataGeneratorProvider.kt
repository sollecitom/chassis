package com.element.dpg.libs.chassis.core.utils

import com.element.dpg.libs.chassis.configuration.utils.StandardEnvironment
import com.element.dpg.libs.chassis.configuration.utils.instanceInfo
import com.element.dpg.libs.chassis.configuration.utils.randomSeed
import com.element.dpg.libs.chassis.core.domain.identity.InstanceInfo
import com.element.dpg.libs.chassis.core.domain.identity.factory.UniqueIdFactory
import com.element.dpg.libs.chassis.core.domain.identity.factory.invoke
import com.element.dpg.libs.chassis.kotlin.extensions.number.toByteArray
import com.element.dpg.libs.chassis.logger.core.loggable.Loggable
import kotlinx.datetime.Clock
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import java.security.SecureRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom

internal class CoreDataGeneratorProvider(private val environment: Environment, initialisedInstanceInfo: InstanceInfo? = null, initialisedClock: Clock? = null, seed: ByteArray? = null) : Loggable(), CoreDataGenerator {

    override val secureRandom: SecureRandom = SecureRandom(seed ?: initialiseSecureRandomSeed())
    override val random: Random = secureRandom.asKotlinRandom()
    override val clock: Clock = initialisedClock ?: initialiseClock()
    private val clusterCoordinates = initialisedInstanceInfo ?: initialiseClusterCoordinates()
    override val newId: UniqueIdFactory by lazy { UniqueIdFactory.invoke(random = random, clock = clock, instanceInfo = clusterCoordinates) }

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

fun CoreDataGenerator.Companion.provider(environment: Environment = StandardEnvironment(), instanceInfo: InstanceInfo? = null, clock: Clock? = null, seed: ByteArray? = null): CoreDataGenerator = CoreDataGeneratorProvider(environment = environment, initialisedInstanceInfo = instanceInfo, initialisedClock = clock, seed = seed)