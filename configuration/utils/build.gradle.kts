dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.cloudnative)
    api(projects.chassisCoreDomain)
    api(projects.chassisLensCoreExtensions)

    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisResourceUtils)
    implementation(libs.http4k.format.jackson.yaml)

    testRuntimeOnly(libs.http4k.format.jackson.yaml)
    testImplementation(projects.chassisTestUtils)
}