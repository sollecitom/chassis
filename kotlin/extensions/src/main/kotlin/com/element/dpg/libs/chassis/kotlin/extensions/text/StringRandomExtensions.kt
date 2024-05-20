package com.element.dpg.libs.chassis.kotlin.extensions.text

import com.element.dpg.libs.chassis.kotlin.extensions.text.CharacterGroups.digitsAndLetters
import kotlin.random.Random

fun Random.string(wordLength: Int, alphabet: Iterable<Char> = digitsAndLetters): String = strings(wordLength..wordLength, alphabet).iterator().next()

fun Random.string(wordLengths: IntRange, alphabet: Iterable<Char> = digitsAndLetters): String = strings(wordLengths, alphabet).iterator().next()

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