package org.sollecitom.chassis.web.service.domain

// TODO refactor this thing
interface WebServiceInfo {

    val port: Int
    val healthPort: Int
    val host: String
}