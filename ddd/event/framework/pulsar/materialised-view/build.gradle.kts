dependencies {
    api(projects.chassisDddDomain)
    api(projects.chassisDddLoggingUtils)
    api(projects.chassisLoggerCore)
    api(projects.chassisJsonUtils)
    api(projects.chassisPulsarUtils)

//    testImplementation(projects.chassisPulsarAvroSerialization) // TODO swap to Avro
    testImplementation(projects.chassisPulsarJsonSerialization)
    testImplementation(projects.chassisDddEventStoreMemory)
    testImplementation(projects.chassisPulsarTestUtils)
    testImplementation(projects.chassisDddSerializationJson)
    testImplementation(projects.chassisDddTestStubsSerializationJson)
    testImplementation(projects.chassisDddEventStoreTestSpecification)
}