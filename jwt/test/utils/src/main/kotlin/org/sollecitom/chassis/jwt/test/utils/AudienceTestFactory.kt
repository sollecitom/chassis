package org.sollecitom.chassis.jwt.test.utils

import org.jose4j.jwk.OctetKeyPairJsonWebKey
import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.random
import com.element.dpg.libs.chassis.core.utils.RandomGenerator
import org.sollecitom.chassis.jwt.domain.JwtAudience
import org.sollecitom.chassis.jwt.jose4j.utils.X25519JoseAudienceAdapter
import org.sollecitom.chassis.kotlin.extensions.text.string

context(RandomGenerator)
fun newX25519JwtAudience(keyId: String = random.string(wordLength = 6), name: Name = Name.random()): JwtAudience {

    val keyPair = newKeyPair(OctetKeyPairJsonWebKey.SUBTYPE_X25519)
    return X25519JoseAudienceAdapter(keyPair, keyId, name)
}