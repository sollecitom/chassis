dependencies {
    api(projects.chassisJsonUtils)
    api(projects.chassisTestUtils)
    implementation(project(mapOf("path" to ":chassis-correlation-core-domain")))
}