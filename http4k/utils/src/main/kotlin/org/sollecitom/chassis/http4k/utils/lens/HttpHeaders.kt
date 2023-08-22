package org.sollecitom.chassis.http4k.utils.lens

object HttpHeaders {

    val ContentType: HttpHeader get() = ContentTypeHeader
}

private object ContentTypeHeader : HttpHeader {

    override val name = "content-type"
}