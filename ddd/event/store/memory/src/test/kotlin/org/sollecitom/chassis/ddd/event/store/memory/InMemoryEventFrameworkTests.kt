package org.sollecitom.chassis.ddd.event.store.memory

import kotlinx.coroutines.CoroutineScope
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.event.store.test.specification.EventFrameworkTestSpecification

// TODO move to another module or delete
@TestInstance(PER_CLASS)
private class InMemoryEventFrameworkTests : EventFrameworkTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    context(CoroutineScope)
    override fun candidate() = InMemoryEventFramework(scope = this@CoroutineScope)
}