package org.sollecitom.chassis.correlation.core.domain.origin

import org.sollecitom.chassis.core.domain.networking.IpAddress

// TODO add other info e.g. OS, browser, etc.
data class Origin(val ipAddress: IpAddress) {

    companion object
}