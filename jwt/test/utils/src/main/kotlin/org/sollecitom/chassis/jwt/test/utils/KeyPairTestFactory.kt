package org.sollecitom.chassis.jwt.test.utils

import org.jose4j.jwk.OkpJwkGenerator
import org.sollecitom.chassis.core.utils.RandomGenerator
import java.security.KeyPair

context(RandomGenerator)
fun newKeyPair(keyType: String): KeyPair {

    val jwk = OkpJwkGenerator.generateJwk(keyType, null, secureRandom)
    return KeyPair(jwk.publicKey, jwk.privateKey)
}