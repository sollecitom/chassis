package org.sollecitom.chassis.ddd.event.stream.memory

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.event.stream.test.specification.EventStreamTestSpecification

@TestInstance(PER_CLASS)
private class InMemoryEventStreamTests : EventStreamTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    override fun eventStream() = InMemoryEventStream()
}