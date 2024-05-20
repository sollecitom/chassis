package com.element.dpg.libs.chassis.test.utils.standard.output

import java.io.PrintStream

enum class StandardPrintStream(private val getStream: () -> PrintStream, private val setStream: (PrintStream) -> Unit) {

    OUT(System::out, System::setOut), ERR(System::err, System::setErr);

    fun get(): PrintStream = getStream()
    fun set(value: PrintStream) = setStream(value)
}