package org.sollecitom.chassis.test.utils.assertions

import assertk.Assert
import assertk.assertions.*
import assertk.assertions.support.expected
import assertk.assertions.support.show
import java.util.*

inline fun <reified ELEMENT> Assert<Set<ELEMENT>>.containsSameElementsAs(other: Set<ELEMENT>) = given { actual ->

    assertThat(actual).containsOnly(*other.toTypedArray())
}

inline fun <reified ELEMENT> Assert<List<ELEMENT>>.containsSameElementsAs(other: List<ELEMENT>) = given { actual ->

    assertThat(actual).containsExactly(*other.toTypedArray())
}

inline fun <reified ELEMENT> Assert<Collection<ELEMENT>>.containsAtLeastAllElementsIn(other: Collection<ELEMENT>) = given { actual ->

    assertThat(actual).containsAtLeast(*other.toTypedArray())
}

fun <KEY, VALUE> Assert<Map<KEY, VALUE>>.containsSameEntriesAs(other: Map<KEY, VALUE>) = given { actual ->

    assertThat(actual.entries).containsSameElementsAs(other.entries)
}

inline fun <reified ERROR : Throwable> Assert<Throwable>.ofType() = given { actual ->

    assertThat(actual).isInstanceOf(ERROR::class)
}

fun Assert<Result<*>>.succeeded() = transform { actual ->

    assertThat(actual.isSuccess).isTrue()
}

fun <RESULT : Any> Assert<Result<RESULT>>.succeededWithResult(expected: RESULT) = given { actual ->

    assertThat(actual).succeeded()
    assertThat(actual.getOrThrow()).isEqualTo(expected)
}

inline fun <reified ERROR : Throwable> Assert<Result<*>>.failedThrowing(): Assert<ERROR> = transform { actual ->

    assertThat(actual).isFailure()
    actual.exceptionOrNull() ?.takeIf { ERROR::class.isInstance(it) }?.let { it as ERROR } ?: expected("failure of type ${ERROR::class} but was:${show(actual.getOrNull())}")
}

inline fun <reified ELEMENT> Assert<Collection<ELEMENT>>.containsExactlyInAnyOrder(other: Collection<ELEMENT>) = given { actual ->

    assertThat(actual).containsExactlyInAnyOrder(*other.toTypedArray())
}

fun Assert<String>.isEqualToIgnoringCase(other: String, locale: Locale = Locale.getDefault()) = given { actual ->

    assertThat(actual.lowercase(locale)).isEqualTo(other.lowercase(locale))
}