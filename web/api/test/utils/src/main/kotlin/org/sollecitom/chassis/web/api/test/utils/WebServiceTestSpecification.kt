package org.sollecitom.chassis.web.api.test.utils

import org.sollecitom.chassis.web.service.domain.WithWebInterface

interface WebServiceTestSpecification : HttpDrivenTestSpecification {

    val service: WithWebInterface
}