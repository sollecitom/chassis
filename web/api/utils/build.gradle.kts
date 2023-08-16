dependencies {
    api(platform(libs.http4k.bom))
    api(projects.chassisHttp4kServerUtils)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}
