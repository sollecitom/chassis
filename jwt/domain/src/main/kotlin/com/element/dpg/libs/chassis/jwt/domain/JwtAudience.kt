package com.element.dpg.libs.chassis.jwt.domain

import java.security.PrivateKey

interface JwtAudience : JwtParty {

    val keyId: String
    val privateKey: PrivateKey
}