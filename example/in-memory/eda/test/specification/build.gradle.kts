dependencies {
    testImplementation(projects.chassisExampleInMemoryEdaDomain)
    testImplementation(projects.chassisExampleInMemoryEdaTestStubs)
    testImplementation(projects.chassisKotlinExtensions)
    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisCorrelationCoreTestUtils)
    testImplementation(projects.chassisMessagingTestUtils)
    testImplementation(projects.chassisHashingUtils) // TODO move to domain
}