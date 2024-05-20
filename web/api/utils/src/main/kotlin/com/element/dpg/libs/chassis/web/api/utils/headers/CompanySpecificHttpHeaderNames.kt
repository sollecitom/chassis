package com.element.dpg.libs.chassis.web.api.utils.headers

internal class CompanySpecificHttpHeaderNames(private val companyName: String, override val gateway: com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames.Gateway) : com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames {

    override val correlation: com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames.Correlation = CorrelationHeaderNames(companyName)

    private class CorrelationHeaderNames(private val companyName: String) : com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames.Correlation {

        override val invocationContext = "x-$companyName-invocation-context"
    }
}