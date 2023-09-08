package org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.pulsar

// TODO could this be generic and shared across projects?
// TODO pure pulsar vs pulsar and postgres?
class MaterialisedPulsarEventStore

// TODO SqlQueryFactory? Pulsar query factory? In-memory query factory?
interface SqlEventStoreQueryFactory {

    // TODO something that maps an EventStore.Query<EVENT> to something like a WHERE condition?
}