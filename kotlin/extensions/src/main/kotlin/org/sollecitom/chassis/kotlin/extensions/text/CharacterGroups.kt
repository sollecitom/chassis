package org.sollecitom.chassis.kotlin.extensions.text

object CharacterGroups {

    val digits by lazy { '0'..'9' }
    val upperCaseLetters by lazy { 'A'..'Z' }
    val lowercaseCaseLetters by lazy { 'a'..'z' }
    val letters by lazy { upperCaseLetters + lowercaseCaseLetters }
    val digitsAndLetters by lazy { digits + letters }
    val digitsAndLowercaseLetters by lazy { digits + lowercaseCaseLetters }
    val digitsAndUppercaseLetters by lazy { digits + upperCaseLetters }
    val symbols by lazy { ('!'..'/') + (':'..'@') + ('['..'`') + ('{'..'~') + listOf('±', '§', '£') }
    val digitsLettersAndSymbols by lazy { digitsAndLetters + symbols }
}