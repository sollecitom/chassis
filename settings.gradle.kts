@file:Suppress("UnstableApiUsage")

module("test", "utils")
module("test", "containers", "utils")
module("kotlin", "extensions")
module("configuration", "utils")
module("lens", "core", "extensions")
module("core", "domain")
module("core", "utils")
module("core", "test", "utils")
module("core", "serialization", "json")
module("correlation", "core", "test", "utils")
module("correlation", "logging", "test", "utils")
module("correlation", "logging", "utils")
module("correlation", "core", "domain")
module("correlation", "core", "serialization", "json")
module("correlation", "origin", "parser")
module("avro", "serialization", "utils")
module("ddd", "domain")
module("ddd", "serialization", "json")
module("ddd", "application")
module("ddd", "logging", "utils")
module("ddd", "test", "stubs")
module("ddd", "test", "stubs", "serialization", "json")
module("ddd", "event", "store", "test", "specification")
module("ddd", "event", "store", "memory")
module("ddd", "event", "store", "postgres")
module("ddd", "event", "framework", "memory")
module("ddd", "event", "framework", "test", "specification")
module("ddd", "event", "framework", "pulsar", "materialised-view")
module("ddd", "event", "framework", "pulsar", "outbox")
module("ddd", "test", "utils")
module("resource", "utils")
module("messaging", "domain")
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
module("openapi", "parser")
module("openapi", "checking", "checker")
module("openapi", "checking", "tests")
module("openapi", "checking", "test", "utils")
module("openapi", "builder")
module("openapi", "validation", "request", "validator")
module("openapi", "validation", "request", "validator", "tests")
module("openapi", "validation", "request", "validator", "test", "utils")
module("openapi", "validation", "http4k", "validator")
module("openapi", "validation", "http4k", "test", "utils")

module("web", "service", "domain")
module("web", "api", "test", "utils")
module("web", "api", "json", "schemas")
module("web", "api", "utils")

module("pulsar", "utils")
module("pulsar", "test", "utils")
module("pulsar", "json", "serialization")
module("pulsar", "avro", "serialization")

module("example", "event", "domain")
module("example", "event", "serialization", "json")
module("example", "event", "serialization", "avro")

module("example", "write-endpoint", "configuration")
module("example", "write-endpoint", "domain")
module("example", "write-endpoint", "application")
module("example", "write-endpoint", "adapters", "driving", "http")
module("example", "write-endpoint", "adapters", "driven", "user", "repository")
module("example", "write-endpoint", "service", "starter")
module("example", "write-endpoint", "service", "test", "specification")
module("example", "write-endpoint", "service", "test", "process-based")
module("example", "write-endpoint", "service", "test", "container-based")

fun module(vararg pathSegments: String) {
    val projectName = pathSegments.last()
    val path = pathSegments.dropLast(1)
    val group = path.joinToString(separator = "-")
    val directory = path.joinToString(separator = "/", prefix = "./")

    include("${rootProject.name}-${if (group.isEmpty()) "" else "$group-"}$projectName")
    project(":${rootProject.name}-${if (group.isEmpty()) "" else "$group-"}$projectName").projectDir = mkdir("$directory/$projectName")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")