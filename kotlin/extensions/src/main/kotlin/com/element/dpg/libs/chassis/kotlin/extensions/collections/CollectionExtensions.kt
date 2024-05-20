package com.element.dpg.libs.chassis.kotlin.extensions.collections

fun <ELEMENT> Collection<ELEMENT>.powerSet(): Set<Set<ELEMENT>> = powerSet(this, setOf(emptySet()))

private tailrec fun <ELEMENT> powerSet(left: Collection<ELEMENT>, accumulated: Set<Set<ELEMENT>>): Set<Set<ELEMENT>> = when {
    left.isEmpty() -> accumulated
    else -> powerSet(left.drop(1), accumulated + accumulated.map { it + left.first() })
}