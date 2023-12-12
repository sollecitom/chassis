dependencies {
    api(projects.chassisMessagingDomain)

    implementation(projects.chassisPulsarUtils)

    testImplementation(projects.chassisMessagingTestUtils)
    testImplementation(projects.chassisPulsarTestUtils)
//    testImplementation(projects.chassisPulsarJsonSerialization) // TODO change to Avro
    testImplementation(projects.chassisLoggingStandardSlf4jConfiguration)
}