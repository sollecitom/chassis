dependencies {
    api(projects.chassisLoggerCore)
    api(projects.chassisLoggerJsonFormatter)

    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassis.chassisJsonTestUtils)
    testRuntimeOnly(projects.chassisLoggerSlf4jAdapter)
}