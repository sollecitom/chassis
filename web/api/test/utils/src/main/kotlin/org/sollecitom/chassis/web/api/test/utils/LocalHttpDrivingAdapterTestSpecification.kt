package org.sollecitom.chassis.web.api.test.utils

interface LocalHttpDrivingAdapterTestSpecification {

    fun path(value: String) = "http://localhost:0/$value"
}