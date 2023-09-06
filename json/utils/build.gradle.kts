dependencies {
    api(libs.org.json)
    api(libs.json.schema.parser) {
        exclude(group = "commons-collections", module = "commons-collections")
    }
    api(projects.chassisKotlinExtensions)

    implementation(projects.chassisResourceUtils)

    testImplementation(projects.chassisTestUtils)
}