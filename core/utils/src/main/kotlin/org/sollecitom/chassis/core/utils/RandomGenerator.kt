package org.sollecitom.chassis.core.utils

import java.security.SecureRandom
import kotlin.random.Random

interface RandomGenerator {

    val random: Random
    val secureRandom: SecureRandom
}