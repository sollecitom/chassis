dependencies {
    implementation(projects.chassisExampleServiceWriteEndpointAdaptersDrivingWeb)
    implementation(projects.chassisExampleServiceWriteEndpointAdaptersDrivenPulsar)
    implementation(projects.chassisExampleServiceWriteEndpointConfiguration)

    implementation(projects.chassisConfigurationUtils)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}