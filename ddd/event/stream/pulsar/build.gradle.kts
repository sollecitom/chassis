dependencies {
    api(projects.chassisDddDomain)
    api(projects.chassisDddLoggingUtils)
    api(projects.chassisLoggerCore)
    api(projects.chassisAvroSerializationUtils)

    implementation(projects.chassisPulsarAvroSerialization) // TODO api?

    testImplementation(projects.chassisPulsarTestUtils)
    testImplementation(projects.chassisDddEventStreamTestSpecification)
}