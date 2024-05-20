package com.element.dpg.libs.chassis.jwt.domain

import kotlinx.datetime.Instant
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.naming.Name

interface JWT {

    val id: String
    val subject: String
    val claimsAsJson: JSONObject
    val issuerName: Name
    val audienceNames: List<Name>
    val issuedAt: Instant
    val expirationTime: Instant?
    val notBeforeTime: Instant?

    fun hasClaim(name: String): Boolean

    fun getStringListClaimValue(name: String): List<String>

    fun getStringClaimValue(name: String): String
}