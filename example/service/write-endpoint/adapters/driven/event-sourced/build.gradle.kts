dependencies {
    api(projects.chassisExampleServiceWriteEndpointDomain)

    implementation(projects.chassisDddEventStoreMemory)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}