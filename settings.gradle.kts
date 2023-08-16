@file:Suppress("UnstableApiUsage")

rootProject.name = "chassis"
val versionCatalogName: String by settings

module("test", "utils")
module("kotlin", "extensions")
module("configuration", "utils")
module("lens", "core", "extensions")
module("core", "domain")
module("core", "utils")
module("core", "test", "utils")
module("ddd", "domain")
module("ddd", "test", "utils")
module("resource", "utils")
module("json", "utils")
module("json", "test", "utils")
module("hashing", "utils")
module("cryptography", "domain")
module("cryptography", "test", "specification")
module("cryptography", "implementation", "bouncy-castle")
module("logger", "core")
module("logger", "slf4j", "adapter")
module("logger", "slf4j", "example")
module("logger", "slf4j", "example")
module("logger", "json", "formatter")
module("logging", "standard", "configuration")
module("logging", "standard", "slf4j", "configuration")
module("http4k", "utils")
module("http4k", "server", "utils")

module("web", "service", "domain")
module("web", "api", "test", "utils")
module("web", "api", "utils")

module("example", "service", "write-endpoint", "configuration")
module("example", "service", "write-endpoint", "domain")
module("example", "service", "write-endpoint", "application")
module("example", "service", "write-endpoint", "adapters", "driving", "web")
module("example", "service", "write-endpoint", "adapters", "driven", "pulsar")
module("example", "service", "write-endpoint", "starter")

module("example", "webapp", "htmx", "starter")
module("example", "webapp", "kweb", "configuration")
module("example", "webapp", "kweb", "starter")

module("example", "webapp", "vaadin", "configuration")
module("example", "webapp", "vaadin", "domain")
module("example", "webapp", "vaadin", "domain", "sdk")
module("example", "webapp", "vaadin", "application")
module("example", "webapp", "vaadin", "adapters", "driving", "web")
module("example", "webapp", "vaadin", "adapters", "driven", "sdk", "memory")
module("example", "webapp", "vaadin", "adapters", "driven", "sdk", "http")
module("example", "webapp", "vaadin", "starter")

fun module(vararg pathSegments: String) {
    val projectName = pathSegments.last()
    val path = pathSegments.dropLast(1)
    val group = path.joinToString(separator = "-")
    val directory = path.joinToString(separator = "/", prefix = "./")

    include("${rootProject.name}-${if (group.isEmpty()) "" else "$group-"}$projectName")
    project(":${rootProject.name}-${if (group.isEmpty()) "" else "$group-"}$projectName").projectDir = mkdir("$directory/$projectName")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")