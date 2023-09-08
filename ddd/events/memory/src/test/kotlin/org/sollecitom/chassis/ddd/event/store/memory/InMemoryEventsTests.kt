package org.sollecitom.chassis.ddd.event.store.memory

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.event.store.test.specification.EventsTestSpecification

@TestInstance(PER_CLASS)
private class InMemoryEventsTests : EventsTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    override fun events() = InMemoryEvents()
}