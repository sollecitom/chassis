dependencies {
    api(projects.chassisCoreDomain)
    api(projects.chassisDddDomain)

    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisTestUtils)
}
