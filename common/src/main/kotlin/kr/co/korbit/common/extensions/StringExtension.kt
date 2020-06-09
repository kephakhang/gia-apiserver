package kr.co.korbit.common.extensions

import java.security.MessageDigest


fun kotlin.String.md5(): String {
    return this.hashString("MD5")
}

fun kotlin.String.sha256(): String {
    return this.hashString("SHA-256")
}

fun kotlin.String.hashString(algorithm: String): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(this.toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}

fun kotlin.String.asResource(work: (String) -> Unit) {
    val content = this.javaClass::class.java.getResource(this).readText()
    work(content)
}
