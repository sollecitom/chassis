dependencies {
    api(projects.chassisCryptographyDomain)

    implementation(libs.bcprov)
    implementation(libs.bcpkix)
    implementation(libs.bcutil)
    implementation(projects.chassisHashingUtils)
    implementation(projects.chassisKotlinExtensions)

    testImplementation(projects.chassisCryptographyTestSpecification)
}