dependencies {
    api(platform(libs.http4k.bom))
    api(projects.chassisCorrelationCoreSerializationJson)
    api(libs.http4k.core)
    api(libs.http4k.format.core)
    api(projects.chassisJsonUtils)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}