dependencies {
    api(libs.org.json)
    api(libs.json.schema.parser)

    implementation(projects.chassisResourceUtils)

//    testImplementation(libs.google.guava)
    testImplementation(projects.chassisTestUtils)
}