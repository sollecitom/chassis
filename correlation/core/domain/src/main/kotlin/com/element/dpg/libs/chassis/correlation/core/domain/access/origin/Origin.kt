package com.element.dpg.libs.chassis.correlation.core.domain.access.origin

import org.sollecitom.chassis.core.domain.networking.IpAddress
import com.element.dpg.libs.chassis.web.client.info.domain.ClientInfo

data class Origin(val ipAddress: IpAddress, val clientInfo: ClientInfo) {

    companion object
}