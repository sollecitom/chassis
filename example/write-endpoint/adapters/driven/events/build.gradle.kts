dependencies {
    api(projects.chassisExampleWriteEndpointDomain)

    implementation(projects.chassisDddEventFrameworkMemory) // TODO replace with Pulsar based one
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisDddTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisDddEventFrameworkMemory)
}