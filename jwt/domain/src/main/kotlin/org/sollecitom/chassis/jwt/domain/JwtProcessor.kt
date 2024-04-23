package org.sollecitom.chassis.jwt.domain

interface JwtProcessor {

    fun readAndVerify(jwt: String): JWT

    fun readWithoutVerifying(jwt: String): JWT

    data class Configuration(
            val requireSubject: Boolean,
            val requireIssuedAt: Boolean,
            val requireExpirationTime: Boolean,
            val maximumFutureValidityInMinutes: Int?,
            val acceptableSignatureAlgorithms: Set<String>,
            val acceptableEncryptionKeyEstablishmentAlgorithms: Set<String>,
            val acceptableContentEncryptionAlgorithms: Set<JwtContentEncryptionAlgorithm>
    )

    companion object
}