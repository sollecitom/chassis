package org.sollecitom.chassis.example.gateway.adapters.driving.http

import org.jose4j.jwk.OctetKeyPairJsonWebKey
import org.jose4j.jwk.OkpJwkGenerator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class JwtExampleTests {

    @Test
    fun `encrypted JWT issuance and verification using RFC8037 Ed25519 EdDSA and X25519 ECDH`() { // TODO continue with https://bitbucket.org/b_c/jose4j/wiki/JWT%20Examples#markdown-header-producing-and-consuming-signed-and-encrypted-jwt-using-rfc8037s-ed25519-eddsa-and-x25519-ecdh

        val issuerJwk = OkpJwkGenerator.generateJwk(OctetKeyPairJsonWebKey.SUBTYPE_ED25519).also { it.keyId = "sender key" }
        val receiverJwk = OkpJwkGenerator.generateJwk(OctetKeyPairJsonWebKey.SUBTYPE_X25519).also { it.keyId = "receiver key" }
    }
}