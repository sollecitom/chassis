package com.element.dpg.libs.chassis.core.domain.identity.factory.tsid

import kotlinx.datetime.Clock
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.kotlin.extensions.bytes.requiredBits
import kotlin.random.Random

internal class TsidVariantSelectorAdapter(private val random: Random, private val clock: Clock) : TsidVariantSelector {

    override val default = DefaultTsidFactoryAdapter(random, clock)

    override fun nodeSpecific(instanceInfo: InstanceInfo) = NodeSpecificTsidFactoryAdapter(instanceInfo.id, instanceInfo.maximumInstancesCount.requiredBits, random, clock)
}