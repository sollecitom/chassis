package org.sollecitom.chassis.core.utils

import kotlinx.datetime.Clock

interface TimeGenerator {

    val clock: Clock
}