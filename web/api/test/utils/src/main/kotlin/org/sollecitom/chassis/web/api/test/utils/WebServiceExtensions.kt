package org.sollecitom.chassis.web.api.test.utils

import org.sollecitom.chassis.web.service.domain.WebServiceInfo

fun WebServiceInfo.httpURLWithPath(path: String, port: Int = this.port, protocol: HttpProtocol = HttpProtocol.HTTP) = "${protocol.value}://$host:$port/$path"