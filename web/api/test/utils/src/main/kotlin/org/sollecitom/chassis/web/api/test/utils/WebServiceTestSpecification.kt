package org.sollecitom.chassis.web.api.test.utils

import org.sollecitom.chassis.web.service.domain.WebService

interface WebServiceTestSpecification : HttpDrivenTestSpecification {

    val webService: WebService
}