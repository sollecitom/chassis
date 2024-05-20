package com.element.dpg.libs.chassis.web.api.test.utils

interface LocalHttpDrivingAdapterTestSpecification {

    fun path(value: String) = "http://localhost:0/$value"
}