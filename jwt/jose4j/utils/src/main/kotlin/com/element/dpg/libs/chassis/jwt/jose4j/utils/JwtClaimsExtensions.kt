package com.element.dpg.libs.chassis.jwt.jose4j.utils

import kotlinx.datetime.Instant
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate

var JwtClaims.issuingTime: Instant
    get() = issuedAt.toInstant()
    set(value) {
        issuedAt = value.toNumericDate()
    }

var JwtClaims.expiryTime: Instant?
    get() = expirationTime?.toInstant()
    set(value) {
        expirationTime = value?.toNumericDate()
    }

var JwtClaims.notBeforeTime: Instant?
    get() = notBefore?.toInstant()
    set(value) {
        notBefore = value?.toNumericDate()
    }

private fun Instant.toNumericDate() = toEpochMilliseconds().let(NumericDate::fromMilliseconds)
private fun NumericDate.toInstant() = valueInMillis.let(Instant::fromEpochMilliseconds)