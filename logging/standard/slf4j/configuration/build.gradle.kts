dependencies {
    api(projects.chassisLoggingStandardConfiguration)
    runtimeOnly(projects.chassisLoggerSlf4jAdapter)

    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisJsonTestUtils)
}