package org.sollecitom.chassis.lens.core.extensions

import org.http4k.lens.LensExtractor
import org.http4k.lens.LensFailure

fun <IN, OUT> LensExtractor<IN, OUT>.asKotlinResult(): LensExtractor<IN, Result<OUT>> = object : LensExtractor<IN, Result<OUT>> {

    override fun invoke(target: IN): Result<OUT> = try {
        Result.success(this@asKotlinResult.invoke(target))
    } catch (e: LensFailure) {
        Result.failure(e)
    }
}