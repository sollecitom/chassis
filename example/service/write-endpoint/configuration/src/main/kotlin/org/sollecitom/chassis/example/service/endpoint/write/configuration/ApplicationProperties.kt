package org.sollecitom.chassis.example.service.endpoint.write.configuration

object ApplicationProperties {

    const val SERVICE_STARTED_LOG_MESSAGE = "--Service started--"

    const val SERVICE_PORT = "SERVICE_PORT"
    const val HEALTH_PORT = "HEALTH_PORT"

    const val SERVICE_OCI_IMAGE_NAME = "example-write-endpoint:snapshot"
    const val SERVICE_OCI_IMAGE_REPOSITORY = "ghcr.io/sollecitom-chassis/"

    const val OPEN_API_FILE_LOCATION = "api/api.yaml"
}