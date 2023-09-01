dependencies {
    api(projects.chassisCorrelationCoreDomain)
    api(projects.chassisCorrelationCoreSerializationJson)
    api(projects.chassisJsonTestUtils)
    api(projects.chassisCorrelationCoreTestUtils)
    api(projects.chassisCoreTestUtils)
    api(projects.chassisTestUtils)

    implementation(projects.chassisKotlinExtensions)
}