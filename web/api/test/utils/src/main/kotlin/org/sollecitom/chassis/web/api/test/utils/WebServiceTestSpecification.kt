package org.sollecitom.chassis.web.api.test.utils

import org.sollecitom.chassis.web.service.domain.WebInterface

interface WebServiceTestSpecification : HttpDrivenTestSpecification {

    val webService: WebInterface
}