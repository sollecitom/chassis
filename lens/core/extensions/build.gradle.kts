dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.cloudnative)
    api(libs.http4k.core)
    api(projects.chassisCoreDomain)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}