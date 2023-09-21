package org.sollecitom.chassis.correlation.core.domain.access.origin

import org.sollecitom.chassis.core.domain.networking.IpAddress

// TODO add other info e.g. OS, browser, etc. // TODO add client-info from web-client-info-domain
data class Origin(val ipAddress: IpAddress) {

    companion object
}