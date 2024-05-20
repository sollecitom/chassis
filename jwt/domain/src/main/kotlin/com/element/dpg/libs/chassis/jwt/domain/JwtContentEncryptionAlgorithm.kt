package com.element.dpg.libs.chassis.jwt.domain

enum class JwtContentEncryptionAlgorithm(val value: String) {
    AES_128_CBC_HMAC_SHA_256("A128CBC-HS256"), AES_192_CBC_HMAC_SHA_384("A192CBC-HS384"), AES_256_CBC_HMAC_SHA_512("A256CBC-HS512"), AES_128_GCM("A128GCM"), AES_192_GCM("A192GCM"), AES_256_GCM("A256GCM")
}