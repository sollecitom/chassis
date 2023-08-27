dependencies {
    api(projects.chassisDddDomain)
    api(projects.chassisCorrelationCoreDomain)
    api(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
}