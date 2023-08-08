dependencies {
    api(projects.chassisExampleServiceWriteEndpointDomain)

    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)

    testImplementation(projects.chassisExampleServiceWriteEndpointConfiguration)
    testImplementation(projects.chassisDddTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisTestUtils)
}