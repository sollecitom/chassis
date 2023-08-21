dependencies {
    api(projects.chassisOpenapiCheckingChecker)
    api(projects.chassisOpenapiBuilder)
    api(projects.chassisTestUtils)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisResourceUtils)
}