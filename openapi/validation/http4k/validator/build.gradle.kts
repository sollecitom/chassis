dependencies {
    api(projects.chassisOpenapiValidationRequestValidator)
    api(projects.chassisHttp4kUtils)

    implementation(projects.chassisKotlinExtensions)
    implementation(projects.chassisJsonUtils)

    testImplementation(projects.chassisOpenapiBuilder)
    testImplementation(projects.chassisKotlinExtensions)
    testImplementation(projects.chassisResourceUtils)
    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisLoggingStandardSlf4jConfiguration)
}