dependencies {
    api(libs.swagger.parser)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisResourceUtils)
    testImplementation(projects.chassisTestUtils)
}