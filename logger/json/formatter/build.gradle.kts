dependencies {
    api(projects.chassisLoggerCore)
    implementation(projects.chassisJsonUtils)

//    testImplementation(libs.google.guava)
    testImplementation(projects.chassisTestUtils)
}