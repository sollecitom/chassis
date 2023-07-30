dependencies {
    api(projects.chassisHttp4kServerUtils)
    api(platform(libs.http4k.bom))

    implementation(projects.chassisLensCoreExtensions)
    implementation(libs.http4k.server.jetty)
    implementation(projects.chassisConfigurationUtils)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}