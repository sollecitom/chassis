dependencies {
    api(projects.chassisExampleCommandEndpointDomain)

    implementation(projects.chassisExampleCommandEndpointConfiguration)
    implementation(projects.chassisPulsarMessagingAdapter)
    implementation(projects.chassisPulsarUtils)

    implementation(projects.chassisExampleEventSerializationJson) // TODO replace with Avro
    implementation(projects.chassisLensCoreExtensions)
    implementation(projects.chassisJsonUtils)
    implementation(projects.chassisConfigurationUtils)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisCorrelationLoggingTestUtils)
    testImplementation(projects.chassisResourceUtils)
    testImplementation(projects.chassisPulsarTestUtils)
}