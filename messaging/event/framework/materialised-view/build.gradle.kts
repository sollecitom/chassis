dependencies {
    api(projects.chassisDddDomain)
    api(projects.chassisDddLoggingUtils)
    api(projects.chassisLoggerCore)
    api(projects.chassisJsonUtils) // TODO remove
    api(projects.chassisPulsarUtils)
    api(projects.chassisMessagingDomain)

    testImplementation(projects.chassisDddEventStoreMemory)
}