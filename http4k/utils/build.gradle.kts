dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.core)
    api(libs.org.json)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}