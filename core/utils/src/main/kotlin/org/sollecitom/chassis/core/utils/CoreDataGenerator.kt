package org.sollecitom.chassis.core.utils

import kotlinx.datetime.Clock
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import kotlin.random.Random

// TODO split into 3 traits
interface CoreDataGenerator {

    val random: Random
    val clock: Clock
    val newId: UniqueIdFactory

    companion object
}