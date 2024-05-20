package com.element.dpg.libs.chassis.web.api.test.utils

import com.element.dpg.libs.chassis.ddd.application.Application
import org.http4k.core.HttpHandler

interface LocalCommandBasedHttpDrivingAdapterTestSpecification : LocalHttpDrivingAdapterTestSpecification {

    fun httpDrivingAdapter(application: Application): HttpHandler
}

