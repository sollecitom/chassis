dependencies {
    api(projects.chassisDddDomain)

    testImplementation(projects.chassisPulsarTestUtils)
    testImplementation(projects.chassisDddEventStreamTestSpecification)
}