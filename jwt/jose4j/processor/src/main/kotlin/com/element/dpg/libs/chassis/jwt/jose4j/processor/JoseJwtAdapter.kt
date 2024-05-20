package com.element.dpg.libs.chassis.jwt.jose4j.processor

import kotlinx.datetime.Instant
import org.jose4j.jwt.JwtClaims
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.jwt.domain.JWT

internal class JoseJwtAdapter(private val delegate: JwtClaims) : JWT {

    override val id: String get() = delegate.jwtId
    override val subject: String get() = delegate.subject
    override val claimsAsJson = delegate.toJson().let(::JSONObject)
    override val issuerName = delegate.issuer.let(::Name)
    override val audienceNames = delegate.audience.map(::Name)
    override val issuedAt: Instant = delegate.issuedAt.let { Instant.fromEpochMilliseconds(it.valueInMillis) }
    override val expirationTime: Instant? = delegate.expirationTime?.let { Instant.fromEpochMilliseconds(it.valueInMillis) }
    override val notBeforeTime: Instant? = delegate.notBefore?.let { Instant.fromEpochMilliseconds(it.valueInMillis) }

    override fun hasClaim(name: String): Boolean = delegate.hasClaim(name)

    override fun getStringListClaimValue(name: String): List<String> = delegate.getStringListClaimValue(name)
    override fun getStringClaimValue(name: String): String = delegate.getStringClaimValue(name)
}