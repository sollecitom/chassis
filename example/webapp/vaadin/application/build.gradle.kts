dependencies {
    api(projects.chassisExampleWebappVaadinDomain)
    api(projects.chassisExampleWebappVaadinDomainSdk)

    testImplementation(projects.chassisExampleWebappVaadinConfiguration)
    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
}