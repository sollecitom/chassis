package com.element.dpg.libs.chassis.test.utils.output

import java.io.ByteArrayOutputStream
import java.io.PrintStream

inline fun <T> withCapturedStandardOutput(standardPrintStream: StandardPrintStream = StandardPrintStream.OUT, action: () -> T): Pair<T, List<String>> {

    val initialPrintStream = standardPrintStream.get()
    try {
        val outputStreamCaptor = ByteArrayOutputStream()
        val printStream = PrintStream(outputStreamCaptor)
        System.setOut(printStream)
        return printStream.use {
            val result = action()
            printStream.flush()
            outputStreamCaptor.flush()
            result to outputStreamCaptor.let { outputStreamCaptor.toString().lines().filterNot(String::isBlank) }
        }
    } finally {
        standardPrintStream.set(initialPrintStream)
    }
}
