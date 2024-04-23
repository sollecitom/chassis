package org.sollecitom.chassis.jwt.jose4j.issuer

import org.jose4j.jwe.JsonWebEncryption
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers
import org.jose4j.jwk.OctetKeyPairJsonWebKey
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.jwt.domain.JwtAudience
import org.sollecitom.chassis.jwt.domain.JwtContentEncryptionAlgorithm
import org.sollecitom.chassis.jwt.domain.JwtIssuer
import java.security.KeyPair
import java.security.PublicKey

class ED25519JoseIssuerAdapter(private val keyPair: KeyPair, private val keyId: String, override val name: Name) : JwtIssuer {

    init {
        require(keyPair.public.algorithm == EDDSA_ALGORITHM) { "Public key must use ${OctetKeyPairJsonWebKey.SUBTYPE_ED25519}" }
        require(keyPair.private.algorithm == EDDSA_ALGORITHM) { "Private key must use ${OctetKeyPairJsonWebKey.SUBTYPE_ED25519}" }
    }

    override val publicKey: PublicKey get() = keyPair.public

    override fun issueJwt(claims: JSONObject): String {

        val jws = JsonWebSignature()
        jws.payload = claims.toString()
        jws.key = keyPair.private
        jws.keyIdHeaderValue = keyId
        jws.algorithmHeaderValue = EDDSA_ALGORITHM
        return jws.compactSerialization
    }

    override fun encryptJwt(innerJwt: String, audience: JwtAudience, contentEncryptionAlgorithm: JwtContentEncryptionAlgorithm): String {

        val jwe = JsonWebEncryption()
        jwe.algorithmHeaderValue = KeyManagementAlgorithmIdentifiers.ECDH_ES
        jwe.encryptionMethodHeaderParameter = contentEncryptionAlgorithm.value
        jwe.key = audience.publicKey
        jwe.keyIdHeaderValue = audience.keyId
        jwe.contentTypeHeaderValue = OUTER_JWT_CONTENT_TYPE_HEADER_VALUE
        jwe.payload = innerJwt
        return jwe.compactSerialization
    }

    companion object {
        private const val OUTER_JWT_CONTENT_TYPE_HEADER_VALUE = "JWT"
        private const val EDDSA_ALGORITHM = AlgorithmIdentifiers.EDDSA
    }
}