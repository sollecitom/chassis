package org.sollecitom.chassis.kotlin.extensions.text

import org.sollecitom.chassis.kotlin.extensions.text.CharacterGroups.digitsAndLetters
import kotlin.random.Random

fun Random.strings(wordLength: Int, alphabet: Iterable<Char> = digitsAndLetters): Sequence<String> = strings(wordLength..wordLength, alphabet)

fun Random.strings(wordLengths: IntRange, alphabet: Iterable<Char> = digitsAndLetters): Sequence<String> {

    require(wordLengths.first > 0)
    require(wordLengths.last >= wordLengths.first)
    val wordLengthsList = wordLengths.toList()
    val chars = chars(alphabet).iterator()
    return sequence {
        while (true) {
            val worldLength = wordLengthsList.random(this@strings)
            val next = generateSequence(chars::next).take(worldLength).joinToString("")
            yield(next)
        }
    }
}

fun Random.chars(alphabet: Iterable<Char> = digitsAndLetters): Sequence<Char> {

    val chars = alphabet.toList()
    return generateSequence { chars.random(this) }
}