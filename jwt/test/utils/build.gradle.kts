dependencies {
    api(projects.chassisCoreTestUtils)
    api(projects.chassisDddTestUtils)
    api(projects.chassisTestUtils)
    api(projects.chassisJsonTestUtils)
    api(projects.chassisJwtJose4jUtils)

    implementation(projects.chassisJwtJose4jIssuer)
    implementation(projects.chassisJwtJose4jProcessor)
}