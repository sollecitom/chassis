dependencies {
    api(projects.chassisExampleWriteEndpointDomain)

    implementation(projects.chassisMessagingEventFrameworkMaterialisedView)
    implementation(projects.chassisPulsarMessagingAdapter)
    implementation(projects.chassisMessagingConfigurationUtils)
    implementation(projects.chassisDddEventStoreMemory) // TODO switch to postgres
    implementation(projects.chassisExampleEventSerializationJson)
    implementation(projects.chassisExampleWriteEndpointConfiguration)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisPulsarJsonSerialization) // TODO switch to Avro

    testImplementation(projects.chassisDddEventFrameworkMemory)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisDddTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisTestUtils)
}