dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.cloudnative)
    api(projects.chassisCoreDomain)

    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisResourceUtils)
    implementation(libs.http4k.format.jackson.yaml)

    testImplementation(projects.chassisLensCoreExtensions)
    testRuntimeOnly(libs.http4k.format.jackson.yaml)
    testImplementation(projects.chassisTestUtils)
}