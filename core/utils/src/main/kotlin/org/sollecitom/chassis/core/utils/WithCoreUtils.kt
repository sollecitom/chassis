package org.sollecitom.chassis.core.utils

import kotlinx.datetime.Clock
import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.configuration.utils.StandardEnvironment
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import kotlin.random.Random

interface WithCoreUtils {

    val random: Random
    val clock: Clock
    val newId: UniqueIdFactory

    companion object
}