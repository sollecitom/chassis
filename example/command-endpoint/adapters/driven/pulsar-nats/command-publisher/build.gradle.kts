dependencies {
    api(projects.chassisExampleCommandEndpointDomain)
    api(projects.chassisMessagingConfigurationUtils)

    implementation(projects.chassisPulsarJsonSerialization) // TODO replace with Avro
    implementation(projects.chassisExampleSharedModelSerializationJson) // TODO replace with Avro
    implementation(projects.chassisJsonUtils) // TODO replace with Avro

    implementation(projects.chassisExampleCommandEndpointConfiguration)
    implementation(projects.chassisPulsarMessagingAdapter)
    implementation(projects.chassisPulsarUtils)

    implementation(projects.chassisLensCoreExtensions)
    implementation(projects.chassisConfigurationUtils)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisCorrelationLoggingUtils)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisCorrelationLoggingTestUtils)
    testImplementation(projects.chassisResourceUtils)
    testImplementation(projects.chassisPulsarTestUtils)
}