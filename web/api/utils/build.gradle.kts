dependencies {
    api(platform(libs.http4k.bom))
    api(projects.chassisCorrelationCoreSerializationJson)
    api(projects.chassisHttp4kServerUtils)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}
