dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.core)
    api(projects.chassisCoreDomain)
    api(projects.chassisConfigurationUtils)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}