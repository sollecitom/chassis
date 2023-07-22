dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.cloudnative)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisLensCoreExtensions)
    testRuntimeOnly(libs.http4k.format.jackson.yaml)
    testImplementation(projects.chassisTestUtils)
}