package org.sollecitom.chassis.web.client.info.analyzer

import org.sollecitom.chassis.web.client.info.domain.ClientInfo

interface ClientInfoAnalyzer {

    operator fun invoke(requestHeaders: Map<String, String>): ClientInfo

    companion object
}
