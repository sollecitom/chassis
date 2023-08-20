dependencies {
    api(libs.org.json)
    api(libs.json.schema.parser) // TODO fix this vulnerability

    implementation(projects.chassisResourceUtils)

//    testImplementation(libs.google.guava)
    testImplementation(projects.chassisTestUtils)
}