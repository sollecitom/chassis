package org.sollecitom.chassis.web.api.test.utils

import org.http4k.client.AsyncHttpHandler
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface HttpDrivenTestSpecification {

    val timeout: Duration get() = 30.seconds
    val httpClient: AsyncHttpHandler
}