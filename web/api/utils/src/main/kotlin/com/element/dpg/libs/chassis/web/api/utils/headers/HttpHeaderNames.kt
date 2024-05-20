package com.element.dpg.libs.chassis.web.api.utils.headers

interface HttpHeaderNames {

    val correlation: com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames.Correlation
    val gateway: com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames.Gateway

    interface Correlation {

        val invocationContext: String
    }

    interface Gateway {

    }

    companion object
}

fun com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames.Companion.of(companyName: String): _root_ide_package_.com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames = _root_ide_package_.com.element.dpg.libs.chassis.web.api.utils.headers.CompanySpecificHttpHeaderNames(companyName = companyName, gateway = _root_ide_package_.com.element.dpg.libs.chassis.web.api.utils.headers.StandardGatewayHeaderNames)

private object StandardGatewayHeaderNames : _root_ide_package_.com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames.Gateway {

}