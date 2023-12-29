package org.sollecitom.chassis.web.api.test.utils

import org.http4k.core.HttpHandler
import org.sollecitom.chassis.ddd.application.Application

interface LocalHttpDrivingAdapterTestSpecification { // TODO move

    fun path(value: String) = "http://localhost:0/$value"

    fun httpDrivingAdapter(application: Application): HttpHandler
}