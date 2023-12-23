dependencies {
    api(projects.chassisMessagingDomain)

    implementation(projects.chassisPulsarUtils)

    testImplementation(projects.chassisMessagingTestUtils)
    testImplementation(projects.chassisPulsarTestUtils)
    testImplementation(projects.chassisLoggingStandardSlf4jConfiguration)
}