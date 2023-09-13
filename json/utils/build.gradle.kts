dependencies {
    api(libs.org.json)
    api(projects.chassisKotlinExtensions)

    implementation(libs.json.schema.parser) {
        exclude(group = "commons-collections", module = "commons-collections")
    }
    implementation(projects.chassisResourceUtils)

    testImplementation(projects.chassisTestUtils)
}