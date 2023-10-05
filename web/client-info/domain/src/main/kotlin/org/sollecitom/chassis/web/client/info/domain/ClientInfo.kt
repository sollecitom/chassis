package org.sollecitom.chassis.web.client.info.domain

data class ClientInfo(val device: Device, val operatingSystem: OperatingSystem, val layoutEngine: LayoutEngine, val agent: Agent) {

    companion object
}