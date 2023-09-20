dependencies {
    api(projects.chassisExampleWriteEndpointDomain)
    api(projects.chassisDddEventFrameworkPulsarMaterialisedView) // TODO hide
    api(projects.chassisDddEventStoreMemory) // TODO hide

    implementation(projects.chassisExampleEventSerializationJson)
    implementation(projects.chassisExampleWriteEndpointConfiguration)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisPulsarJsonSerialization)

    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisDddTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisTestUtils)
}