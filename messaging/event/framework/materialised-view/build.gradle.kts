dependencies {
    api(projects.chassisDddDomain)
    api(projects.chassisDddLoggingUtils)
    api(projects.chassisLoggerCore)
    api(projects.chassisJsonUtils)
    api(projects.chassisPulsarUtils)
    api(projects.chassisMessagingDomain)

    testImplementation(projects.chassisDddEventStoreMemory)
}