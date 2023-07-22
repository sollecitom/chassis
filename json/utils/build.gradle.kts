dependencies {
    api(libs.org.json)
    api(libs.everit.json.schema)

    implementation(projects.chassisResourceUtils)

//    testImplementation(libs.google.guava)
    testImplementation(projects.chassisTestUtils)
}