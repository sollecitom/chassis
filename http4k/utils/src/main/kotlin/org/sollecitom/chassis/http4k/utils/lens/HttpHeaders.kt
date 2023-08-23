package org.sollecitom.chassis.http4k.utils.lens

object HttpHeaders {

    val ContentType: HttpHeader get() = ContentTypeHeader
    val ContentLength: HttpHeader get() = ContentLengthHeader
    val Location: HttpHeader get() = LocationHeader
}

private object ContentTypeHeader : HttpHeader {

    override val name = "content-type"
}

private object ContentLengthHeader : HttpHeader {

    override val name = "content-length"
}

private object LocationHeader : HttpHeader {

    override val name = "location"
}