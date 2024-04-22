package org.sollecitom.chassis.web.api.utils.headers

internal class CompanySpecificHttpHeaderNames(private val companyName: String, override val gateway: HttpHeaderNames.Gateway) : HttpHeaderNames {

    override val correlation: HttpHeaderNames.Correlation = CorrelationHeaderNames(companyName)

    private class CorrelationHeaderNames(private val companyName: String) : HttpHeaderNames.Correlation {

        override val invocationContext = "x-$companyName-invocation-context"
    }
}