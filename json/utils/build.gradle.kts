dependencies {
    api(libs.org.json)
    api(libs.everit.json.schema) // TODO upgrade commons-collections in it to 4.3+

    implementation(projects.chassisResourceUtils)

//    testImplementation(libs.google.guava)
    testImplementation(projects.chassisTestUtils)
}