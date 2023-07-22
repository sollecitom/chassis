dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.core)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}