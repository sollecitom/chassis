package com.element.dpg.libs.chassis.web.client.info.domain

import org.sollecitom.chassis.core.domain.naming.Name

data class Agent(val className: Name?, val name: Name?, val version: Version?) {

    companion object
}