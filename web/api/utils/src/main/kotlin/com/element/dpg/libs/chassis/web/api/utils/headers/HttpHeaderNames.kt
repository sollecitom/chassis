package com.element.dpg.libs.chassis.web.api.utils.headers

interface HttpHeaderNames {

    val correlation: Correlation
    val gateway: Gateway

    interface Correlation {

        val invocationContext: String
    }

    interface Gateway

    companion object
}

fun HttpHeaderNames.Companion.of(companyName: String): HttpHeaderNames = CompanySpecificHttpHeaderNames(companyName = companyName, gateway = StandardGatewayHeaderNames)

private object StandardGatewayHeaderNames : HttpHeaderNames.Gateway