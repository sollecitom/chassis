dependencies {
    api(projects.chassisDddEventStreamPulsar)
    api(projects.chassisDddEventStorePostgres)
    api(projects.chassisDddEventStoreMemory) // TODO remove

    testImplementation(projects.chassisPulsarTestUtils)
    testImplementation(projects.chassisDddEventFrameworkTestSpecification)
}