package kr.co.korbit.common.extensions

import java.security.MessageDigest
import java.util.regex.Pattern


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

fun kotlin.String.isEmail(): Boolean {
    return "^[a-zA-Z0-9_+&*-]+(?:\\\\.[a-zA-Z0-9_+&*-]+ )*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,7}$".toRegex().matches(this)
}

fun kotlin.String.isPhoneNumber(): Boolean {
    return "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*\$".toRegex().matches(this)
}