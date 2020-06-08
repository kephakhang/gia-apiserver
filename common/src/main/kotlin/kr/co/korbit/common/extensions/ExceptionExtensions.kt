package kr.co.korbit.common.extensions

import java.io.PrintWriter
import java.io.StringWriter

// Extension property on Exception
val kotlin.Exception.stackTraceString: String
    get() {
        val stringWriter = StringWriter()
        this.printStackTrace(PrintWriter(stringWriter))
        return stringWriter.toString()
    }
