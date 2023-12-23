dependencies {
    api(projects.chassisMessagingDomain)

    implementation(projects.chassisPulsarUtils)

    testImplementation(projects.chassisLoggingStandardSlf4jConfiguration)
    testImplementation(projects.chassisDddEventFrameworkTestSpecification)
//    testImplementation(projects.chassisPulsarAvroSerialization)
    testImplementation(projects.chassisPulsarJsonSerialization) // TODO swap to Avro
    testImplementation(projects.chassisDddEventStoreMemory)
    testImplementation(projects.chassisPulsarTestUtils)
    testImplementation(projects.chassisMessagingTestUtils)
    testImplementation(projects.chassisDddSerializationJson)
    testImplementation(projects.chassisDddTestStubsSerializationJson)
}