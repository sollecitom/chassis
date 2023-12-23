package org.sollecitom.chassis.ddd.event.framework.memory

import kotlinx.coroutines.CoroutineScope
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.framework.EventFramework
import org.sollecitom.chassis.ddd.event.framework.test.specification.EventFrameworkTestSpecification

@TestInstance(PER_CLASS)
private class InMemoryEventFrameworkTests : EventFrameworkTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    context(CoroutineScope)
    override fun candidate() = EventFramework.Mutable.inMemory()
}