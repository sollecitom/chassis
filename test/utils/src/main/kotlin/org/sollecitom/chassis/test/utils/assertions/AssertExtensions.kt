package org.sollecitom.chassis.test.utils.assertions

import assertk.Assert
import assertk.assertions.*
import java.util.*

inline fun <reified ELEMENT> Assert<Collection<ELEMENT>>.containsSameElementsAs(other: Collection<ELEMENT>) = given { actual ->

    assertThat(actual).containsOnly(*other.toTypedArray())
}

inline fun <reified ELEMENT> Assert<Collection<ELEMENT>>.containsAllElementsIn(other: Collection<ELEMENT>) = given { actual ->

    assertThat(actual).containsAll(*other.toTypedArray())
}

fun <KEY, VALUE> Assert<Map<KEY, VALUE>>.containsSameEntriesAs(other: Map<KEY, VALUE>) = given { actual ->

    assertThat(actual.entries).containsSameElementsAs(other.entries)
}

inline fun <reified ERROR : Throwable> Assert<Throwable>.ofType() = given { actual ->

    assertThat(actual).isInstanceOf(ERROR::class)
}

fun Assert<Result<*>>.succeeded() = given { actual ->

    assertThat(actual.isSuccess).isTrue()
}

fun <RESULT : Any> Assert<Result<RESULT>>.succeededWithResult(expected: RESULT) = given { actual ->

    assertThat(actual).succeeded()
    assertThat(actual.getOrThrow()).isEqualTo(expected)
}

inline fun <reified ERROR : Throwable> Assert<Result<*>>.failedThrowing() = given { actual ->

    assertThat(actual.isFailure).isTrue()
    assertThat(actual.exceptionOrNull()).isNotNull().ofType<ERROR>()
}

inline fun <reified ELEMENT> Assert<Collection<ELEMENT>>.containsExactlyInAnyOrder(other: Collection<ELEMENT>) = given { actual ->

    assertThat(actual).containsExactlyInAnyOrder(*other.toTypedArray())
}

fun Assert<String>.isEqualToIgnoringCase(other: String, locale: Locale = Locale.getDefault()) = given { actual ->

    assertThat(actual.lowercase(locale)).isEqualTo(other.lowercase(locale))
}