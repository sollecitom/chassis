package org.sollecitom.chassis.web.client.info.domain

import org.sollecitom.chassis.core.domain.naming.Name

data class Device(val className: Name?, val name: Name?, val brand: Name?) {

    companion object
}