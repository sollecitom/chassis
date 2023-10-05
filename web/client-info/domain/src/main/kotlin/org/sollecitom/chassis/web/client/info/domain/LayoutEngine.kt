package org.sollecitom.chassis.web.client.info.domain

import org.sollecitom.chassis.core.domain.naming.Name

data class LayoutEngine(val className: Name?, val name: Name?, val version: Version?) {

    companion object
}