dependencies {
    api(platform(libs.http4k.bom))
    api(projects.chassisCorrelationCoreSerializationJson)
    api(projects.chassisHttp4kServerUtils)
    api(projects.chassisDddApplication)
    api(projects.chassisLoggerCore)
    api(projects.chassisConfigurationUtils)
    api(projects.chassisCorrelationLoggingUtils)
    api(projects.chassisDddDomain)

    implementation(libs.http4k.server.jetty)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}
