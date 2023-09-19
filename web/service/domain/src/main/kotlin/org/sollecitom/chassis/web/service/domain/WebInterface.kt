package org.sollecitom.chassis.web.service.domain

interface WebInterface { // TODO turn into an interface that has a web interface

    val host: String
    val port: Int
    val healthPort: Int

    companion object {

        fun create(host: String, port: Int, healthPort: Int): WebInterface = WebInterfaceData(host, port, healthPort)
    }
}

private data class WebInterfaceData(override val host: String, override val port: Int, override val healthPort: Int) : WebInterface