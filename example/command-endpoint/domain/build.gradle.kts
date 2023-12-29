dependencies {
    api(projects.chassisExampleEventDomain)

    testImplementation(projects.chassisExampleCommandEndpointConfiguration)
    testImplementation(projects.chassisTestUtils)
}