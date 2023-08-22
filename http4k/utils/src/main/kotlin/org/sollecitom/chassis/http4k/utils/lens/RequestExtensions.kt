package org.sollecitom.chassis.http4k.utils.lens

import org.http4k.core.ContentType
import org.http4k.core.Request
import org.http4k.lens.Header

val Request.contentType: ContentType? get() = Header.CONTENT_TYPE(this)