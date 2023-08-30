dependencies {
    api(projects.chassisExampleServiceWriteEndpointApplication)
    api(projects.chassisWebApiUtils)

    implementation(projects.chassisExampleServiceWriteEndpointConfiguration)
    implementation(platform(libs.http4k.bom))
    implementation(libs.http4k.server.jetty)
    implementation(projects.chassisCorrelationCoreSerializationJson)
    implementation(projects.chassisLensCoreExtensions)
    implementation(projects.chassisJsonUtils)
    implementation(projects.chassisConfigurationUtils)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)
    runtimeOnly(projects.chassisWebApiJsonSchemas)

    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisOpenapiCheckingTestUtils)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisResourceUtils)
    testImplementation(projects.chassisOpenapiValidationHttp4kTestUtils)
}