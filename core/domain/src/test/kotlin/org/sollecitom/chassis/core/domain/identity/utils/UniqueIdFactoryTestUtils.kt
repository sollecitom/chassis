package org.sollecitom.chassis.core.domain.identity.utils

import kotlinx.datetime.Clock
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import kotlin.random.Random

operator fun UniqueIdFactory.Companion.invoke(random: Random = Random, clock: Clock = Clock.System, nodeId: Int = 0, maximumNodesCount: Int = 256): UniqueIdFactory = UniqueIdFactory.invoke(random, clock, nodeId, maximumNodesCount)