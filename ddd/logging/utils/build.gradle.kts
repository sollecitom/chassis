dependencies {
    api(projects.chassisDddDomain)
    api(projects.chassisLoggerCore)
    api(projects.chassisKotlinExtensions)
    api(projects.chassisCorrelationLoggingUtils)

    testImplementation(projects.chassisTestUtils)
}