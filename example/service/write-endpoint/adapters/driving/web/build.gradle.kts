dependencies {
    api(projects.chassisExampleServiceWriteEndpointApplication)
    api(projects.chassisWebApiUtils)

    implementation(projects.chassisExampleServiceWriteEndpointConfiguration)
    implementation(platform(libs.http4k.bom))
    implementation(libs.http4k.server.jetty)
    implementation(projects.chassisLensCoreExtensions)
    implementation(projects.chassisJsonUtils)
    implementation(projects.chassisConfigurationUtils)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisOpenapiCheckingTestUtils)
    testImplementation(projects.chassisResourceUtils)
}