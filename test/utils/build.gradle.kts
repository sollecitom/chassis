dependencies {
    api(platform(libs.kotlinx.coroutines.bom))
    api(libs.kotlinx.coroutines.test)

    api(libs.junit.jupiter.api)
    api(libs.junit.jupiter.params)
    api(libs.assertk)

    runtimeOnly(libs.junit.jupiter.engine)
}