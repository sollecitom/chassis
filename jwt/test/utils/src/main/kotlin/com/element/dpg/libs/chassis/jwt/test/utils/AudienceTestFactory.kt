package com.element.dpg.libs.chassis.jwt.test.utils

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.random
import com.element.dpg.libs.chassis.core.utils.RandomGenerator
import com.element.dpg.libs.chassis.jwt.domain.JwtAudience
import com.element.dpg.libs.chassis.jwt.jose4j.utils.X25519JoseAudienceAdapter
import com.element.dpg.libs.chassis.kotlin.extensions.text.string
import org.jose4j.jwk.OctetKeyPairJsonWebKey

context(RandomGenerator)
fun newX25519JwtAudience(keyId: String = random.string(wordLength = 6), name: Name = Name.random()): JwtAudience {

    val keyPair = newKeyPair(OctetKeyPairJsonWebKey.SUBTYPE_X25519)
    return X25519JoseAudienceAdapter(keyPair, keyId, name)
}