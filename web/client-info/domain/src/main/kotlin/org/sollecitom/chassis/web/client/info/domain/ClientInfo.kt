package org.sollecitom.chassis.web.client.info.domain

interface ClientInfo {

    val device: Device
    val operatingSystem: OperatingSystem
    val layoutEngine: LayoutEngine
    val agent: Agent
}