dependencies {
    api(projects.chassisCorrelationCoreDomain)
    api(projects.chassisJsonUtils)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisJsonTestUtils)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
    testImplementation(projects.chassisTestUtils)
}