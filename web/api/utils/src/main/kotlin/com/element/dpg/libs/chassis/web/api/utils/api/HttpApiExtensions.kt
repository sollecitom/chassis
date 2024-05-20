package com.element.dpg.libs.chassis.web.api.utils.api

import com.element.dpg.libs.chassis.core.domain.networking.RequestedPort
import com.element.dpg.libs.chassis.web.api.utils.endpoint.Endpoint
import com.element.dpg.libs.chassis.web.api.utils.filters.StandardHttpFilter
import org.http4k.cloudnative.health.Health
import org.http4k.cloudnative.health.ReadinessCheck
import org.http4k.core.Filter
import org.http4k.core.HttpHandler

context(HttpApiDefinition)
fun mainHttpApi(endpoints: Set<Endpoint>, requestedPort: RequestedPort, requestFilter: Filter = StandardHttpFilter.forRequests(), responseFilter: Filter = StandardHttpFilter.forResponses()) = HttpApi(endpoints, requestedPort, requestFilter, responseFilter)

fun healthHttpApi(requestedPort: RequestedPort, checks: List<ReadinessCheck> = emptyList()) = HttpApi(standardHealthApp(checks), requestedPort)

fun standardHealthApp(checks: List<ReadinessCheck> = emptyList()): HttpHandler = Health(checks = checks)