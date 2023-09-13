dependencies {
    api(projects.chassisExampleWriteEndpointDomain)
    api(projects.chassisDddApplication)

    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)

    testImplementation(projects.chassisExampleWriteEndpointConfiguration)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisDddTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisTestUtils)

    testImplementation(projects.chassisDddEventStoreMemory) // TODO remove and replace?
    testImplementation(projects.chassisExampleWriteEndpointAdaptersDrivenEvents) // TODO remove and replace with a mock?
}