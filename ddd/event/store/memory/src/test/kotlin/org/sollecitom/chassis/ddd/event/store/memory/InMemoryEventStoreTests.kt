package org.sollecitom.chassis.ddd.event.store.memory

import kotlinx.coroutines.CoroutineScope
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.event.store.test.specification.EventStoreTestSpecification

@TestInstance(PER_CLASS)
private class InMemoryEventStoreTests : EventStoreTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    context(CoroutineScope)
    override fun candidate() = InMemoryEventStore()

    // TODO add tests about querying
}