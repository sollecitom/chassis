dependencies {
    api(libs.org.json)
    api(libs.json.schema.parser) // TODO fix this vulnerability
    api(projects.chassisKotlinExtensions)

    implementation(projects.chassisResourceUtils)

    testImplementation(projects.chassisTestUtils)
}