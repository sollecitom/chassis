package org.sollecitom.chassis.jwt.domain

import org.json.JSONObject

interface JwtIssuer : JwtParty {

    fun issueEncryptedJwt(claims: JSONObject, audience: JwtAudience, contentEncryptionAlgorithm: JwtContentEncryptionAlgorithm = JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512): String {

        val innerJwt = issueJwt(claims)
        return encryptJwt(innerJwt, audience, contentEncryptionAlgorithm)
    }

    fun issueJwt(claims: JSONObject): String

    fun encryptJwt(innerJwt: String, audience: JwtAudience, contentEncryptionAlgorithm: JwtContentEncryptionAlgorithm): String
}