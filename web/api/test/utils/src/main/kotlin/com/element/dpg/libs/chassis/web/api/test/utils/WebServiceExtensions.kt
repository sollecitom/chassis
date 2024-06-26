package com.element.dpg.libs.chassis.web.api.test.utils

import com.element.dpg.libs.chassis.core.domain.networking.Port
import com.element.dpg.libs.chassis.core.domain.networking.http.HttpProtocol
import com.element.dpg.libs.chassis.web.service.domain.WithWebInterface

fun WithWebInterface.httpURLWithPath(path: String, port: Port = webInterface.port, protocol: HttpProtocol = HttpProtocol.HTTP) = "${protocol.value}://${webInterface.host}:${port.value}/$path"