package org.sollecitom.chassis.example.write_endpoint.configuration

// TODO rename to ServiceProperties, and move under service/starter
object ApplicationProperties {

    const val SERVICE_STARTED_LOG_MESSAGE = "--Service started--"
    const val SERVICE_STOPPED_LOG_MESSAGE = "--Service stopped--"

    // TODO move these in the container-based test module
    const val SERVICE_PORT = "SERVICE_PORT"
    const val HEALTH_PORT = "HEALTH_PORT"
    const val PULSAR_BROKER_URI = "PULSAR_BROKER_URI"
    const val PULSAR_TOPIC = "PULSAR_TOPIC"
    const val PULSAR_CONSUMER_INSTANCE_ID = "PULSAR_CONSUMER_INSTANCE_ID"

    const val SERVICE_OCI_IMAGE_NAME = "example-write-endpoint:snapshot"
    const val SERVICE_OCI_IMAGE_REPOSITORY = "ghcr.io/sollecitom-chassis/"
}