dependencies {
    api(projects.chassisOpenapiParser)
    api(libs.swagger.request.validator) {
        exclude(group = "commons-codec", module = "commons-codec")
    }

    implementation(projects.chassisKotlinExtensions)
}