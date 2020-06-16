package kr.co.korbit.gia.util

import kotlin.experimental.and

class NumberUtil {
    companion object {
        private val lowerHexArray = "0123456789abcdef".toCharArray()
        private val upperHexArray = "0123456789ABCDEF".toCharArray()
        fun copyIntToByteArray(bytes: ByteArray, offset: Int, d: Int) {
            for (i in 0..3) {
                bytes[offset + i] = (d shr (4 - i - 1 shl 3)).toByte()
            }
        }

        fun copyLongToByteArray(bytes: ByteArray, offset: Int, l: Long) {
            for (i in 0..7) {
                bytes[offset + i] = (l shr (8 - i - 1 shl 3)).toByte()
            }
        }

        private val hexArray = "0123456789ABCDEF".toCharArray()
        fun bytesToHexString(bytes: ByteArray, upper: Boolean = false): String {
            val hexChars = CharArray(bytes.size * 2)
            for (j in bytes.indices) {
                val v = (bytes[j] and 0xFF.toByte()).toInt()

                hexChars[j * 2] = hexArray[v ushr 4]
                hexChars[j * 2 + 1] = hexArray[v and 0x0F]
            }
            if( upper )
                return String(hexChars)
            else
                return String(hexChars).toLowerCase()
        }

        fun isDigit(string: String): Boolean {
            for (c in string.toCharArray()) {
                if (Character.isDigit(c) == false) return false
            }
            return true
        }
    }
}