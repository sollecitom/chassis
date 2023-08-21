package org.sollecitom.chassis.kotlin.extensions.text

object CharacterGroups {

    val digits by lazy { '0'..'9' }
    val upperCaseLetters by lazy { 'A'..'Z' }
    val lowercaseCaseLetters by lazy { 'a'..'z' }
    val letters by lazy { (upperCaseLetters + lowercaseCaseLetters).toSet() }
    val digitsAndLetters by lazy { (digits + letters).toSet() }
    val digitsAndLowercaseLetters by lazy { (digits + lowercaseCaseLetters).toSet() }
    val digitsAndUppercaseLetters by lazy { (digits + upperCaseLetters).toSet() }
    val symbols by lazy { (('!'..'/') + (':'..'@') + ('['..'`') + ('{'..'~') + listOf('±', '§', '£')).toSet() }
    val digitsLettersAndSymbols by lazy { (digitsAndLetters + symbols).toSet() }
}