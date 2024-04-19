package org.sollecitom.chassis.web.api.utils.api

import org.http4k.cloudnative.health.Health
import org.http4k.cloudnative.health.ReadinessCheck
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.sollecitom.chassis.core.domain.networking.RequestedPort
import org.sollecitom.chassis.web.api.utils.endpoint.Endpoint
import org.sollecitom.chassis.web.api.utils.filters.StandardHttpFilter

context(HttpApiDefinition)
fun mainHttpApi(endpoints: Set<Endpoint>, requestedPort: RequestedPort, requestFilter: Filter = StandardHttpFilter.forRequests(), responseFilter: Filter = StandardHttpFilter.forResponses()) = HttpApi(endpoints, requestedPort, requestFilter, responseFilter)

fun healthHttpApi(requestedPort: RequestedPort, app: HttpHandler = standardHealthApp()) = HttpApi(app, requestedPort)

fun standardHealthApp(checks: List<ReadinessCheck> = emptyList()): HttpHandler = Health(checks = checks)