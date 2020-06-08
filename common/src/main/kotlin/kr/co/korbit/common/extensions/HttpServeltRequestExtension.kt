package kr.co.korbit.common.extensions

import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.traceString() : String{
    return this.inputStream.bufferedReader().use { it.readText() }
}