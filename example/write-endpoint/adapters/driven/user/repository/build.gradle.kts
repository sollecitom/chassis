dependencies {
    api(projects.chassisExampleWriteEndpointDomain)

    implementation(projects.chassisDddEventFrameworkPulsarMaterialisedView) // TODO remove and replace with pulsarMessagingEventFrameworkMaterialisedView
    implementation(projects.chassisDddEventStoreMemory) // TODO switch to postgres
    implementation(projects.chassisExampleEventSerializationJson)
    implementation(projects.chassisExampleWriteEndpointConfiguration)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisPulsarJsonSerialization)

    testImplementation(projects.chassisDddEventFrameworkMemory)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisDddTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisTestUtils)
}