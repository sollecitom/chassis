package org.sollecitom.chassis.web.service.domain

// TODO refactor this thing
interface WebServiceInfo {

    val host: String
    val port: Int
    val healthPort: Int

    companion object {

        fun create(host: String, port: Int, healthPort: Int): WebServiceInfo = WebServiceInfoData(host, port, healthPort)
    }
}

private data class WebServiceInfoData(override val host: String, override val port: Int, override val healthPort: Int) : WebServiceInfo