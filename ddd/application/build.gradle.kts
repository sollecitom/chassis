dependencies {
    api(projects.chassisDddDomain)
    api(projects.chassisCorrelationCoreDomain)
    api(projects.chassisCorrelationLoggingUtils)
    api(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
}