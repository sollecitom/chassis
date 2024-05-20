package com.element.dpg.libs.chassis.web.api.test.utils

import org.http4k.core.HttpHandler
import com.element.dpg.libs.chassis.ddd.application.dispatching.Application

interface LocalCommandBasedHttpDrivingAdapterTestSpecification : LocalHttpDrivingAdapterTestSpecification {

    fun httpDrivingAdapter(application: Application): HttpHandler
}

