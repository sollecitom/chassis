package org.sollecitom.chassis.example.write_endpoint.configuration

object ServiceProperties {

    const val SERVICE_STARTED_LOG_MESSAGE = "--Service started--"
    const val SERVICE_STOPPED_LOG_MESSAGE = "--Service stopped--"

    const val SERVICE_PORT = "SERVICE_PORT"
    const val HEALTH_PORT = "HEALTH_PORT"
    const val PULSAR_BROKER_URI = "PULSAR_BROKER_URI"
    const val PULSAR_TOPIC = "PULSAR_TOPIC"
    const val PULSAR_CONSUMER_INSTANCE_ID = "PULSAR_CONSUMER_INSTANCE_ID"

    const val INSTANCE_ID = "INSTANCE_ID"
    const val INSTANCE_GROUP_MAX_SIZE = "INSTANCE_GROUP_MAX_SIZE"

    const val SERVICE_OCI_IMAGE_NAME = "example-write-endpoint:snapshot"
    const val SERVICE_OCI_IMAGE_REPOSITORY = "ghcr.io/sollecitom-chassis/"
}