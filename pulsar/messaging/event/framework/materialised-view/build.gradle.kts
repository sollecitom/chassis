dependencies {
    api(projects.chassisDddDomain)
    api(projects.chassisDddLoggingUtils)
    api(projects.chassisLoggerCore)
    api(projects.chassisPulsarUtils)
    api(projects.chassisMessagingDomain)
    api(projects.chassisMessagingEventFrameworkMaterialisedView)

    implementation(projects.chassisPulsarMessagingAdapter)

    testImplementation(projects.chassisDddEventFrameworkTestSpecification)
//    testImplementation(projects.chassisPulsarAvroSerialization)
    testImplementation(projects.chassisPulsarJsonSerialization) // TODO swap to Avro
    testImplementation(projects.chassisDddEventStoreMemory)
    testImplementation(projects.chassisPulsarTestUtils)
    testImplementation(projects.chassisMessagingTestUtils)
    testImplementation(projects.chassisDddSerializationJson)
    testImplementation(projects.chassisDddTestStubsSerializationJson)
}