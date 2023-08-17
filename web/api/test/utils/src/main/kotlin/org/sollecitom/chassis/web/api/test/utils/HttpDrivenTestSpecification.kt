package org.sollecitom.chassis.web.api.test.utils

import org.http4k.core.HttpHandler

interface HttpDrivenTestSpecification {

    val httpClient: HttpHandler
}