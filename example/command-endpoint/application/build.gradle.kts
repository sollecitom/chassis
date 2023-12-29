dependencies {
    api(projects.chassisExampleCommandEndpointDomain)
    api(projects.chassisDddApplication)

    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)

    testImplementation(projects.chassisExampleCommandEndpointConfiguration)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisDddTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisTestUtils)
}