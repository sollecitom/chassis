package com.element.dpg.libs.chassis.core.test.utils

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.utils.RandomGenerator
import com.element.dpg.libs.chassis.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters
import com.element.dpg.libs.chassis.kotlin.extensions.text.strings

context(RandomGenerator)
fun Name.Companion.random(wordLengths: IntRange = 5..10, alphabet: CharRange = lowercaseCaseLetters): Name = random.strings(wordLengths, alphabet).iterator().next().let(::Name)

context(RandomGenerator)
fun Name.Companion.random(wordLength: Int, alphabet: CharRange = lowercaseCaseLetters): Name = random.strings(wordLength, alphabet).iterator().next().let(::Name)