dependencies {
    api(projects.chassisLoggerCore)
    implementation(projects.chassisJsonUtils)
    implementation(projects.chassisResourceUtils)

    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisJsonTestUtils)
}