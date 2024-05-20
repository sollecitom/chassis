package com.element.dpg.libs.chassis.ddd.event.framework.memory

import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.ddd.domain.framework.EventFramework
import com.element.dpg.libs.chassis.ddd.event.framework.test.specification.EventFrameworkTestSpecification
import kotlinx.coroutines.CoroutineScope
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class InMemoryEventFrameworkTests : EventFrameworkTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    context(CoroutineScope)
    override fun candidate() = EventFramework.Mutable.inMemory()
}