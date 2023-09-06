package org.sollecitom.chassis.core.utils

import kotlinx.datetime.Clock
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.long
import org.sollecitom.chassis.configuration.utils.StandardEnvironment
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import org.sollecitom.chassis.logger.core.loggable.Loggable
import kotlin.random.Random

internal class CoreDataGeneratorProvider(private val environment: Environment, initialisedClock: Clock? = null, initialisedRandom: Random? = null) : Loggable(), CoreDataGenerator {

    override val random: Random = initialisedRandom ?: initialiseRandom()
    override val clock: Clock = initialisedClock ?: initialiseClock()
    override val newId: UniqueIdFactory by lazy { UniqueIdFactory.invoke(random = random, clock = clock) }

    private fun initialiseRandom(): Random {

        logger.info { "Reading random seed from property ${randomSeedKey.meta.name}" }
        val seed = randomSeedKey(environment)
        logger.info { "Initialised random from seed: $seed" }
        return Random(seed)
    }

    private fun initialiseClock(): Clock {

        val clock = Clock.System
        logger.info { "Initialised clock to be the system clock" }
        return clock
    }

    companion object {
        val randomSeedKey = EnvironmentKey.long().defaulted("random.seed", Random.nextLong(), "The seed (long) used to initialise random data generation")
    }
}

fun CoreDataGenerator.Companion.provider(environment: Environment = StandardEnvironment(), initialisedClock: Clock? = null, initialisedRandom: Random? = null): CoreDataGenerator = CoreDataGeneratorProvider(environment)