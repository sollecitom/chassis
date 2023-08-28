dependencies {
    api(projects.chassisExampleServiceWriteEndpointDomain)

    implementation(projects.chassisDddStoreMemory)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}