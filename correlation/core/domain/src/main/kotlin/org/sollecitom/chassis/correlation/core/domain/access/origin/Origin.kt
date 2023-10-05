package org.sollecitom.chassis.correlation.core.domain.access.origin

import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.web.client.info.domain.ClientInfo

data class Origin(val ipAddress: IpAddress, val clientInfo: ClientInfo) {

    companion object
}