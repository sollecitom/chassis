package org.sollecitom.chassis.web.api.test.utils

import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.web.service.domain.WithWebInterface

fun WithWebInterface.httpURLWithPath(path: String, port: Port = webInterface.port, protocol: HttpProtocol = HttpProtocol.HTTP) = "${protocol.value}://${webInterface.host}:${port.value}/$path"