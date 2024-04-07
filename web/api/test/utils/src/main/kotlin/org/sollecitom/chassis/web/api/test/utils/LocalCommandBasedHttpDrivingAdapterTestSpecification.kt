package org.sollecitom.chassis.web.api.test.utils

import org.http4k.core.HttpHandler
import org.sollecitom.chassis.ddd.application.Application

interface LocalCommandBasedHttpDrivingAdapterTestSpecification : LocalHttpDrivingAdapterTestSpecification {

    fun httpDrivingAdapter(application: Application): HttpHandler
}

