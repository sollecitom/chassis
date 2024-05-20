package com.element.dpg.libs.chassis.core.domain.identity.factory.tsid

import com.element.dpg.libs.chassis.core.domain.identity.InstanceInfo
import com.element.dpg.libs.chassis.kotlin.extensions.bytes.requiredBits
import kotlinx.datetime.Clock
import kotlin.random.Random

internal class TsidVariantSelectorAdapter(private val random: Random, private val clock: Clock) : TsidVariantSelector {

    override val default = DefaultTsidFactoryAdapter(random, clock)

    override fun nodeSpecific(instanceInfo: InstanceInfo) = NodeSpecificTsidFactoryAdapter(instanceInfo.id, instanceInfo.maximumInstancesCount.requiredBits, random, clock)
}