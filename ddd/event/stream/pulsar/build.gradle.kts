dependencies {
    api(projects.chassisDddDomain)
    api(projects.chassisDddLoggingUtils)
    api(projects.chassisLoggerCore)
    api(projects.chassisJsonUtils)
//    api(projects.chassisAvroSerializationUtils)

    implementation(projects.chassisPulsarJsonSerialization) // TODO api?
//    implementation(projects.chassisPulsarAvroSerialization) // TODO api?

    testImplementation(projects.chassisPulsarTestUtils)
    testImplementation(projects.chassisDddSerializationJson)
    testImplementation(projects.chassisDddEventStreamTestSpecification)
}