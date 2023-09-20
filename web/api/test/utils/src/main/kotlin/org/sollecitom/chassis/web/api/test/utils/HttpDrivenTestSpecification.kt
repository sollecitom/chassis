package org.sollecitom.chassis.web.api.test.utils

import org.http4k.client.AsyncHttpHandler

interface HttpDrivenTestSpecification {

    val httpClient: AsyncHttpHandler
}