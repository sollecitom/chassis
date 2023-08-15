package org.sollecitom.chassis.http4k.utils.lens

import org.http4k.core.Request
import org.http4k.lens.Lens
import org.http4k.lens.Meta
import org.http4k.lens.ParamMeta
import org.http4k.lens.Path

inline fun <reified T> Path.composite(name: String, noinline lensGet: (Request) -> T): Lens<Request, T> {

    val meta = Meta(true, "path", ParamMeta.ObjectParam, name)
    return Lens(meta, lensGet)
}