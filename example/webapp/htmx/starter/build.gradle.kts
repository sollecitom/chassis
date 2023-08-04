dependencies {
    implementation(libs.http4k.server.jetty)
    implementation(projects.chassisExampleServiceWriteEndpointConfiguration)
    implementation(projects.chassisLensCoreExtensions)
    implementation(projects.chassisConfigurationUtils)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
    testImplementation(platform(libs.http4k.bom))
    testImplementation(libs.http4k.client.apache.async)
    testImplementation(libs.http4k.client.apache) // TODO remove this after making the async one work with coroutines
}