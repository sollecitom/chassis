dependencies {
    testImplementation(projects.chassisLoggerCore)
    testImplementation(projects.chassisTestUtils)

    testRuntimeOnly(projects.chassisLoggerSlf4jAdapter)
}