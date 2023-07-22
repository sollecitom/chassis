@file:Suppress("UnstableApiUsage")

rootProject.name = "chassis"
val versionCatalogName: String by settings

module("test", "utils")
module("kotlin", "extensions")
module("configuration", "utils")
module("lens", "core", "extensions")
module("core", "domain")
module("resource", "utils")
module("json", "utils")
module("json", "test", "utils")
module("logger", "core")
module("logger", "slf4j", "adapter")
module("logger", "slf4j", "example")
module("logger", "slf4j", "example")
module("logger", "json", "formatter")

fun module(vararg pathSegments: String) {
    val projectName = pathSegments.last()
    val path = pathSegments.dropLast(1)
    val group = path.joinToString(separator = "-")
    val directory = path.joinToString(separator = "/", prefix = "./")

    include("${rootProject.name}-${if (group.isEmpty()) "" else "$group-"}$projectName")
    project(":${rootProject.name}-${if (group.isEmpty()) "" else "$group-"}$projectName").projectDir = mkdir("$directory/$projectName")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")