package org.sollecitom.chassis.web.api.utils

object HttpHeaders {

    val ContentType: HttpHeader get() = ContentTypeHeader
}

private object ContentTypeHeader : HttpHeader {

    override val name = "content-type"
}