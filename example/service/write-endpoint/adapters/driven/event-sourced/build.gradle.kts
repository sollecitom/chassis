dependencies {
    api(projects.chassisExampleServiceWriteEndpointDomain)

    implementation(projects.chassisDddEventFrameworkMemory)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}