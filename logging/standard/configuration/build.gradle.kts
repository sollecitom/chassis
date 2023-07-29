dependencies {
    api(projects.chassisLoggerCore)
    api(projects.chassisLoggerJsonFormatter)
    api(projects.chassisConfigurationUtils)

    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassis.chassisJsonTestUtils)
    testRuntimeOnly(projects.chassisLoggerSlf4jAdapter)
}