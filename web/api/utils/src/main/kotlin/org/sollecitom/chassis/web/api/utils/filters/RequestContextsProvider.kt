package org.sollecitom.chassis.web.api.utils.filters

import org.http4k.core.RequestContexts

object RequestContextsProvider {

    val requestContexts: RequestContexts by lazy { RequestContexts() }
}