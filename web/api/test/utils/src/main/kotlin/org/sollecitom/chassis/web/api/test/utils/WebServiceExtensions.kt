package org.sollecitom.chassis.web.api.test.utils

import org.sollecitom.chassis.web.service.domain.WebService

fun WebService.httpURLWithPath(path: String, port: Int = this.port) = "http://localhost:$port/$path"