package org.sollecitom.chassis.web.api.origin.parser

import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator

@TestInstance(PER_CLASS)
private class ParsingTheOriginExampleTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `from the user agent added by an HTTP client`() {

        val rawUserAgent = "Apache-HttpAsyncClient/5.2.1 (Java/19.0.2)"
        val headers = mapOf("User-Agent" to rawUserAgent)

        val analyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withAllFields().build()

        val userAgent = analyzer.parse(headers)

        println(userAgent)
    }
}