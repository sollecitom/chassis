package com.element.dpg.libs.chassis.jwt.test.utils

import com.element.dpg.libs.chassis.core.utils.RandomGenerator
import org.jose4j.jwk.OkpJwkGenerator
import java.security.KeyPair

context(RandomGenerator)
fun newKeyPair(keyType: String): KeyPair {

    val jwk = OkpJwkGenerator.generateJwk(keyType, null, secureRandom)
    return KeyPair(jwk.publicKey, jwk.privateKey)
}