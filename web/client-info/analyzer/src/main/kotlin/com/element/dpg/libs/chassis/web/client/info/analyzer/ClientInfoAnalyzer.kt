package com.element.dpg.libs.chassis.web.client.info.analyzer

import com.element.dpg.libs.chassis.web.client.info.domain.ClientInfo

interface ClientInfoAnalyzer {

    operator fun invoke(requestHeaders: Map<String, String>): ClientInfo

    companion object
}
