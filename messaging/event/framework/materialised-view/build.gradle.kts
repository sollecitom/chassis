dependencies {
    api(projects.chassisDddDomain)
    api(projects.chassisDddLoggingUtils)
    api(projects.chassisLoggerCore)
    api(projects.chassisMessagingDomain)

    testImplementation(projects.chassisDddEventStoreMemory)
}