dependencies {
    implementation(libs.kweb.core)
    implementation(projects.chassisLoggingStandardSlf4jConfiguration)
    implementation(projects.chassisKotlinExtensions)

    implementation(projects.chassisExampleWebappKwebConfiguration)

    testImplementation(projects.chassisTestUtils)
}