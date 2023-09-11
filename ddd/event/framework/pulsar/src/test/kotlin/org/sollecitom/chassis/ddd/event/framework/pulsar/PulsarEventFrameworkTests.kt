package org.sollecitom.chassis.ddd.event.framework.pulsar

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.framework.CompositeEventFramework
import org.sollecitom.chassis.ddd.domain.stream.EventStream
import org.sollecitom.chassis.ddd.event.framework.test.specification.EventFrameworkTestSpecification
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore

@TestInstance(PER_CLASS)
@Disabled
private class PulsarEventFrameworkTests : EventFrameworkTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

//    private val pulsar = Pulsar
    override fun events() = pulsarStreamWithMaterializedViewInMemory()

    @BeforeAll
    fun beforeAll() {

    }

    @AfterAll
    fun afterAll() {

    }
}

fun pulsarStreamWithMaterializedViewInMemory(): CompositeEventFramework {

    val pulsarEventStream = EventStream.Mutable.pulsar()
    return CompositeEventFramework(stream = pulsarEventStream, store = InMemoryEventStore())
}

fun pulsarStreamWithMaterializedViewInPostgres(): CompositeEventFramework {

    TODO("implement")
}

fun EventStream.Mutable.Companion.pulsar(): EventStream.Mutable {

    TODO("implement")
}