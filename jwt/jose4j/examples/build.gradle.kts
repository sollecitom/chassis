dependencies {
    testImplementation(projects.chassisJwtDomain)
    testImplementation(projects.chassisJwtJose4jIssuer)
    testImplementation(projects.chassisJwtJose4jProcessor)
    testImplementation(projects.chassisJwtTestUtils)
    testImplementation(projects.chassisLoggingStandardSlf4jConfiguration)

    testImplementation(libs.jose4j) // TODO move to a JWT module and import this as implementation instead
}