package org.sollecitom.chassis.http4k.utils.lens

import org.http4k.client.AsyncHttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend operator fun AsyncHttpHandler.invoke(request: Request): Response = suspendCoroutine { continuation ->

    invoke(request) { response ->
        continuation.resume(response)
    }
}