package org.sollecitom.chassis.ddd.events.pulsar

//import org.junit.jupiter.api.TestInstance
//import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
//import org.sollecitom.chassis.core.domain.identity.Id
//import org.sollecitom.chassis.core.test.utils.testProvider
//import org.sollecitom.chassis.core.utils.CoreDataGenerator
//import org.sollecitom.chassis.ddd.domain.*
//import org.sollecitom.chassis.ddd.events.test.specification.EventsTestSpecification
//
//@TestInstance(PER_CLASS)
//private class PulsarEventsTests : EventsTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {
//
//    override fun events() = PulsarEvents()
//}
//
//class CompositeEvents(stream: EventStream.Mutable<Event>, store: EventStore.Mutable<Event>) : Events.Mutable, EventStream.Mutable<Event> by stream, EventStore.Mutable<Event> by store {
//
//    override fun forEntityId(entityId: Id): EntitySpecificEvents.Mutable {
//        TODO("Not yet implemented")
//    }
//}