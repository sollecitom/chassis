import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.event.store.test.specification.EventStoreTestSpecification

//  TODO implement
//@TestInstance(PER_CLASS)
//private class PulsarEventStoreTests : EventStoreTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {
//
//    override fun eventStore() = InMemoryEventStore()
//}