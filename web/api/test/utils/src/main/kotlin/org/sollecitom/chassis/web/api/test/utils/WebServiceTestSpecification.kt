package org.sollecitom.chassis.web.api.test.utils

import org.sollecitom.chassis.web.service.domain.WebServiceInfo

interface WebServiceTestSpecification : HttpDrivenTestSpecification {

    val webService: WebServiceInfo
}