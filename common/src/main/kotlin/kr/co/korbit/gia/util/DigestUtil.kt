package kr.co.korbit.gia.util

import org.apache.tomcat.util.codec.binary.Base64
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class DigestUtil {
    companion object {
        fun encodeBase64(str: String): String {
            val encodeByte: ByteArray = Base64.encodeBase64(str.toByteArray())
            return String(encodeByte)
        }

        fun decodeBase64(str: String): String {
            val decodeByte: ByteArray = Base64.decodeBase64(str.toByteArray())
            return String(decodeByte)
        }

        fun encryptMD5(str: String): String? {
            var MD5: String? = ""
            MD5 = try {
                val md = MessageDigest.getInstance("MD5")
                md.update(str.toByteArray())
                val byteData = md.digest()
                NumberUtil.bytesToHexString(byteData)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                null
            }
            return MD5
        }

        fun encryptSHA1(str: String): String? {
            var SHA1: String? = ""
            SHA1 = try {
                val sh = MessageDigest.getInstance("SHA1")
                sh.update(str.toByteArray(charset("UTF-8")))
                val digest = sh.digest()
                NumberUtil.bytesToHexString(digest)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                null
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                null
            }
            return SHA1
        }

        fun encryptSHA256(str: String): String? {
            var SHA256: String? = ""
            SHA256 = try {
                val sh = MessageDigest.getInstance("SHA-256")
                sh.update(str.toByteArray(charset("UTF-8")))
                val byteData = sh.digest()
                NumberUtil.bytesToHexString(byteData)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                null
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                null
            }
            return SHA256
        }

        fun encryptSHA256Hmac(deviceNumber: String): String? {
            return DigestUtil.encryptSHA256Hmac("Korbit_Forever^__^;*!!", deviceNumber)
        }

        fun encryptSHA256Hmac(secretKey: String, deviceNumber: String): String? {
            var SHA256: String? = ""
            SHA256 = try {
                val sha256HMAC = Mac.getInstance("HmacSHA256")
                val secretKey = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
                sha256HMAC.init(secretKey)
                val sha256Result = sha256HMAC.doFinal(deviceNumber.toByteArray())
                NumberUtil.bytesToHexString(sha256Result)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            return SHA256
        }

        fun encryptHMACMD5(secretKey: String, value: String): String? {
            var digest: String? = ""
            digest = try {
                val mac = Mac.getInstance("HmacMD5")
                val secretKey = SecretKeySpec(secretKey.toByteArray(charset("UTF-8")), "HmacMD5")
                mac.init(secretKey)
                val bytes = mac.doFinal(value.toByteArray(charset("ASCII")))
                NumberUtil.bytesToHexString(bytes)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            return digest
        }
    }
}