package com.element.dpg.libs.chassis.core.test.utils.stubs

import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.core.utils.provider

val CoreDataGenerator.Companion.testProvider: CoreDataGenerator by lazy { CoreDataGenerator.provider(instanceInfo = testInstanceInfo) }

private val testInstanceInfo = InstanceInfo(id = 0, maximumInstancesCount = 256, groupName = "test-instance-group-name".let(::Name))