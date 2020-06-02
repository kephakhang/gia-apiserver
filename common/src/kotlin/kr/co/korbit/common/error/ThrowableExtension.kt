package kr.co.korbit.exception

import java.io.PrintWriter
import java.io.StringWriter

// Extension property on Throwable
val kotlin.Throwable.stackTraceString: String
    get() {
        val stringWriter = StringWriter()
        this.printStackTrace(PrintWriter(stringWriter))
        return stringWriter.toString()
    }
