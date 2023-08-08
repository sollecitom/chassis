dependencies {
    api(projects.chassisExampleServiceWriteEndpointDomain)

    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisDddTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisTestUtils)
}