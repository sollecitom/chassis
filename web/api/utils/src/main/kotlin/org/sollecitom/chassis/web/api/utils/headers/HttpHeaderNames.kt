package org.sollecitom.chassis.web.api.utils.headers

interface HttpHeaderNames {

    val correlation: Correlation

    interface Correlation {

        val invocationContext: String
    }

    companion object
}

fun HttpHeaderNames.Companion.of(companyName: String): HttpHeaderNames = CompanySpecificHttpHeaderNames(companyName)