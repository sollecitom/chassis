dependencies { // TODO move this whole SDK thing into its own separate project
    api(projects.chassisCoreUtils)
    api(projects.chassisCoreDomain)
    api(projects.chassisDddDomain)

    testImplementation(projects.chassisExampleWebappVaadinConfiguration)
    testImplementation(projects.chassisTestUtils)
    testImplementation(projects.chassisCoreTestUtils)
}