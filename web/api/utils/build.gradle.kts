dependencies {
    api(platform(libs.http4k.bom))
    api(projects.chassisCorrelationCoreSerializationJson)
    api(projects.chassisHttp4kServerUtils)
    api(projects.chassisLoggerCore)
    api(projects.chassisCorrelationLoggingUtils)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}
