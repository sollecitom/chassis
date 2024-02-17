package org.sollecitom.chassis.core.example.inmemory.eda.test.specification

import org.junit.jupiter.api.Test
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.messaging.domain.Topic
import org.sollecitom.chassis.messaging.test.utils.create

private class InMemoryEventDrivenArchitectureTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `one producer and one consumer on a single topic`() {

        val topic = Topic.create()

        println(topic)
    }
}