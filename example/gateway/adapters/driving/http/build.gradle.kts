dependencies {
    api(projects.chassisExampleGatewayApplication)
    api(projects.chassisWebApiUtils)

    implementation(projects.chassisExampleGatewayConfiguration)
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
    testImplementation(projects.chassisWebApiTestUtils)
    testImplementation(projects.chassisOpenapiCheckingTestUtils)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisCorrelationLoggingTestUtils)
    testImplementation(projects.chassisResourceUtils)
    testImplementation(projects.chassisOpenapiValidationHttp4kTestUtils)

    testImplementation(libs.jose4j) // TODO move to a JWT module and import this as implementation instead
}