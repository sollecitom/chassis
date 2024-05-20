package com.element.dpg.libs.chassis.web.service.domain

import com.element.dpg.libs.chassis.core.domain.networking.Port

interface WebInterface {

    val host: String
    val port: Port
    val healthPort: Port

    companion object {

        private const val LOCALHOST = "localhost"

        fun create(host: String, port: Port, healthPort: Port): WebInterface = WebInterfaceData(host, port, healthPort)

        fun local(port: Port, healthPort: Port): WebInterface = create(LOCALHOST, port, healthPort)
    }
}

private data class WebInterfaceData(override val host: String, override val port: Port, override val healthPort: Port) : WebInterface