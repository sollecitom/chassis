package org.sollecitom.chassis.ddd.event.framework.pulsar

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.framework.CompositeEventFramework
import org.sollecitom.chassis.ddd.event.framework.test.specification.EventFrameworkTestSpecification

@TestInstance(PER_CLASS)
@Disabled
private class PulsarEventsTests : EventFrameworkTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    override fun events() = pulsarStreamWithMaterializedViewInPostgres()
}

fun pulsarStreamWithMaterializedViewInMemory(): CompositeEventFramework {

    TODO("implement")
}

fun pulsarStreamWithMaterializedViewInPostgres(): CompositeEventFramework {

    TODO("implement")
}