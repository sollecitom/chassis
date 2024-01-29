package org.sollecitom.chassis.core.test.utils

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.RandomGenerator
import org.sollecitom.chassis.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters
import org.sollecitom.chassis.kotlin.extensions.text.strings

context(RandomGenerator)
fun Name.Companion.random(wordLengths: IntRange = 5..10, alphabet: CharRange = lowercaseCaseLetters): Name = random.strings(wordLengths, alphabet).iterator().next().let(::Name)