dependencies {
    api(projects.chassisCoreUtils)
    api(projects.chassisCoreDomain)
    api(projects.chassisDddDomain)

    testImplementation(projects.chassisExampleWebappVaadinConfiguration)
    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
}