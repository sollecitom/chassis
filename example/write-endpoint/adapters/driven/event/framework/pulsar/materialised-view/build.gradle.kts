dependencies {
    api(projects.chassisExampleWriteEndpointDomain)
    api(projects.chassisDddEventFrameworkPulsarMaterialisedView)

    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisDddTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisTestUtils)
}