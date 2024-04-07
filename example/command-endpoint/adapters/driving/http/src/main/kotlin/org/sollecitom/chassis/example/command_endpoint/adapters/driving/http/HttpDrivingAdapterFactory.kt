package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http

import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.predicate.search.httpCommandHandler
import org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.user.registration.httpCommandHandler
import org.sollecitom.chassis.example.event.domain.predicate.search.FindPredicateDevice
import org.sollecitom.chassis.example.event.domain.user.registration.RegisterUser
import org.sollecitom.chassis.web.api.utils.api.HttpDrivingAdapter

operator fun HttpDrivingAdapter.Companion.invoke(application: Application, configuration: HttpDrivingAdapter.Configuration): HttpDrivingAdapter = CommandEndpointHttpDrivingAdapter(application, configuration, setOf(RegisterUser.httpCommandHandler, FindPredicateDevice.httpCommandHandler))