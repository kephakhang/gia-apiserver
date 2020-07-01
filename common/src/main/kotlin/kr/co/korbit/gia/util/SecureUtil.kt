package kr.co.korbit.gia.util


import kr.co.korbit.common.extensions.stackTraceString
import kr.co.korbit.gia.aop.ControllerProxy
import mu.KotlinLogging
import org.springframework.util.StringUtils
import java.nio.charset.Charset
import java.util.*
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private val logger = KotlinLogging.logger(ControllerProxy::class.java.name)

class SecureUtil{
    companion object SecureUtil {
        val keyBytes: ByteArray =
            byteArrayOf(-20, -96, -100, -20, -99, -76, -19, -120, -84, -20, -105, -108, -20, -89, -79, -19)
        val ivParameterSpec = IvParameterSpec(keyBytes)
        val secretKeySpec = SecretKeySpec(keyBytes, "AES")

        /**
         * @param loginId
         * @param password
         * @return
         * @Description        : 비밀번호 단방향 암호화 메소드 (loginId도 반드시 필요)
         * @Author            : angelkum
         * @Date                : 오후 2:21:18
         */
        fun encryptPassword(loginId: String, password: String): String? {
            val secret = loginId + "(zhqlt)"
            val key = password + "(fhdpqj)"

            var sha256: String? = null
            try {
                val sha256_HMAC = Mac.getInstance("HmacSHA256")
                val secret_key = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
                sha256_HMAC.init(secret_key)
                val sha256_result = sha256_HMAC.doFinal(key.toByteArray())
                sha256 = NumberUtil.bytesToHexString(sha256_result)
            } catch (e: java.lang.Exception) {
                logger.error{ e.stackTraceString }
            }
            return sha256
        }

        /**
         * @param mobileNumber
         * @return
         * @Description        : 메소드이름에서 알 수 있듯이 핸드폰번호 전용 암호화 메소드
         * @Author            : angelkum
         * @Date                : 오후 2:22:58
         */
        fun encryptMobileNumber(mobileNumber: String): String? {
            return try {
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
                val encrypted = cipher.doFinal("$mobileNumber.zhqltvhdpqj".toByteArray(charset("UTF-8")))
                String(Base64.getEncoder().encode(encrypted))
            } catch (e: Exception) {
                logger.error("input=$mobileNumber : ${e.stackTraceString}")
                null
            }
        }

        /**
         * @param mobileNumber
         * @return
         * @Description        : 메소드이름에서 알 수 있듯이 핸드폰번호 전용 복호화 메소드
         * @Author            : angelkum
         * @Date                : 오후 2:23:23
         */
        fun decryptMobileNumber(mobileNumber: String): String? {
            return if (StringUtils.isEmpty(mobileNumber)) mobileNumber else try {
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

                //logger.debug("decryptMobileNumber : " +  mobileNumber) ;
                val encrypted = Base64.getDecoder().decode(mobileNumber)
                //logger.debug("encryptedMobileNumber.length : " +  encrypted.length) ;
                val decrypted = String(cipher.doFinal(encrypted), Charset.forName("UTF-8"))
                val index = decrypted.indexOf('.')
                decrypted.substring(0, index)
            } catch (e: Exception) {
                logger.error("input=$mobileNumber : ${e.stackTraceString}")
                null
            }
        }

        fun decryptBase64(base64: String): String {
            val byteArray: ByteArray = Base64.getDecoder().decode(base64.toByteArray())

            // Print the decoded array
            // System.out.println(Arrays.toString(byteArray));

            // Print the decoded string
            return String(byteArray)
        }

        fun encryptBase64(utf8: String): String {
            val byteArray: ByteArray = Base64.getEncoder().encode(utf8.toByteArray())

            // Print the decoded array
            // System.out.println(Arrays.toString(byteArray));

            // Print the decoded string
            return String(byteArray)
        }

        @JvmStatic
        fun main(argv: Array<String>) {

        }
    }
}