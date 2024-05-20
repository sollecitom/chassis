package com.element.dpg.libs.chassis.web.api.test.utils

import com.element.dpg.libs.chassis.web.service.domain.WithWebInterface

interface WebServiceTestSpecification : HttpDrivenTestSpecification {

    val service: WithWebInterface
}