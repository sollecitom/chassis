dependencies {
    api(projects.chassisCoreTestUtils)
    api(projects.chassisDddTestUtils)
    api(projects.chassisTestUtils)
    api(projects.chassisJsonTestUtils)
    api(libs.jose4j) // TODO move to a JWT module and import this as implementation instead
}