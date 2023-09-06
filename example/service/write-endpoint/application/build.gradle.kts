dependencies {
    api(projects.chassisExampleServiceWriteEndpointDomain)
    api(projects.chassisDddApplication)

    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)

    testImplementation(projects.chassisExampleServiceWriteEndpointConfiguration)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisDddTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisTestUtils)

    testImplementation(projects.chassisExampleServiceWriteEndpointAdaptersDrivenEventSourced) // TODO remove and replace with a mock?
}