package org.sollecitom.chassis.core.domain.identity.utils

import kotlinx.datetime.Clock
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import org.sollecitom.chassis.core.domain.naming.Name
import kotlin.random.Random

operator fun UniqueIdFactory.Companion.invoke(random: Random = Random, clock: Clock = Clock.System, instanceInfo: InstanceInfo = testInstanceInfo): UniqueIdFactory = UniqueIdFactory.invoke(random, clock, instanceInfo)

private val testInstanceInfo = InstanceInfo(id = 0, maximumInstancesCount = 256, groupName = "test-instance-group-name".let(::Name))