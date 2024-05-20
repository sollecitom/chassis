package com.element.dpg.libs.chassis.jwt.test.utils

import org.jose4j.jwt.JwtClaims
import org.json.JSONObject

fun jwtClaimsJson(configure: (JwtClaims) -> Unit): JSONObject = jwtClaims(configure).toJson().let(::JSONObject)

private fun jwtClaims(configure: (JwtClaims) -> Unit): JwtClaims = JwtClaims().also(configure)