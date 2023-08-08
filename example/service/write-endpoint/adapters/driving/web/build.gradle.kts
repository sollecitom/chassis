dependencies {
    api(projects.chassisExampleServiceWriteEndpointApplication)
    api(projects.chassisHttp4kServerUtils)

    api(platform(libs.http4k.bom))
    implementation(libs.http4k.server.jetty)

    implementation(projects.chassisLensCoreExtensions)
    implementation(projects.chassisConfigurationUtils)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}