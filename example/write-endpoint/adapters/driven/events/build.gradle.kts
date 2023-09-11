dependencies {
    api(projects.chassisExampleWriteEndpointDomain)

    implementation(projects.chassisDddEventFrameworkMemory)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}