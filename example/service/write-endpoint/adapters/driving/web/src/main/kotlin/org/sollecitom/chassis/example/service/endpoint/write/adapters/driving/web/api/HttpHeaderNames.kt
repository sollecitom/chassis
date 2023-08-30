package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api

// TODO move
interface HttpHeaderNames {

    val correlation: Correlation

    interface Correlation {

        val invocationContext: String
    }
}

// TODO move
class CompanySpecificHttpHeaderNames(private val companyName: String) : HttpHeaderNames {

    override val correlation: HttpHeaderNames.Correlation = CorrelationHeaderNames(companyName)

    private class CorrelationHeaderNames(private val companyName: String) : HttpHeaderNames.Correlation {

        override val invocationContext = "x-$companyName-invocation-context"
    }
}