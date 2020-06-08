package kr.co.korbit.gia.util

import kr.co.korbit.exception.stackTraceString
import kr.co.korbit.gia.jpa.common.SearchOpt
import mu.KLogger
import mu.KotlinLogging
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

class RedisUtil {
    var redisTemplate: RedisTemplate<*, *>? = null
        get() {
            if (redisTemplate == null) {
                redisTemplate = ApplicationContextProvider.getBean("redisTemplate") as RedisTemplate<*,*>
            }
            return redisTemplate
        }

    fun deleteObj(key: String): Long {
        return redisTemplate!!.opsForHash<String, Any>().delete(key as Nothing)
    }

    fun deleteOpsHash(key: String?, hashKey: String): Long {
        return redisTemplate!!.opsForHash<String, Any>().delete(key as Nothing, hashKey)
    }

    fun getObj(key: String?): Any? {
        return redisTemplate!!.opsForValue().get(key as Nothing)
    }

    fun hScan(key: String, opt: SearchOpt): List<String>? {
        return try {
            val options: ScanOptions =
                ScanOptions.scanOptions().match("*" + opt.search + "*").count(opt.size).build()
            val entries: Cursor<Map.Entry<ByteArray, ByteArray>> =
                redisTemplate!!.getConnectionFactory().getConnection().hScan(key.toByteArray(), options)
            val result: MutableList<String> = ArrayList()
            if (entries != null) {
                while (entries.hasNext()) {
                    val entry: Map.Entry<ByteArray, ByteArray> = entries.next()
                    val actualKey = entry.key
                    result.add(String(actualKey))
                }
                result
            } else {
                null
            }
        } catch (e: Exception) {
            logger.error(e.stackTraceString)
            null
        }
    }

    fun hScanAll(key: String): List<Map<String, String>>? {
        return try {
            val options: ScanOptions = ScanOptions.scanOptions().match("*").count(1000).build()
            val entries: Cursor<Map.Entry<ByteArray, ByteArray>> =
                redisTemplate!!.getConnectionFactory().getConnection().hScan(key.toByteArray(), options)
            val list: MutableList<Map<String, String>> =
                ArrayList()
            if (entries != null) {
                while (entries.hasNext()) {
                    val entry: Map.Entry<ByteArray, ByteArray> = entries.next()
                    val map: MutableMap<String, String> =
                        HashMap()
                    map["key"] = String(entry.key, Charset.forName("UTF-8"))
                    map["value"] = String(entry.value, Charset.forName("UTF-8"))
                    list.add(map)
                }
                list
            } else {
                null
            }
        } catch (e: Exception) {
            logger.error(e.stackTraceString)
            null
        }
    }

    fun hGet(key: String, hashKey: String): String? {
        return try {
            val result: ByteArray = redisTemplate!!.getConnectionFactory().getConnection()
                .hGet(key.toByteArray(), hashKey.toByteArray(charset("UTF-8")))
            String(result)
        } catch (e: Exception) {
            logger.error(e.stackTraceString)
            null
        }
    }

    fun getOpsHash(key: String?, hashKey: String?): Any? {
        return redisTemplate!!.opsForHash<String, Any>().get(key as Nothing, hashKey)
    }

    fun <T> putOpsHash(key: String?, hashKey: String?, Any: Any) {
        redisTemplate!!.opsForHash<String, Any>().put(key as Nothing, hashKey, Any)
    }

    fun hSet(key: String, field: String, value: String) {
        try {
            redisTemplate!!.getConnectionFactory().getConnection()
                .hSet(key.toByteArray(), field.toByteArray(charset("UTF-8")), value.toByteArray())
        } catch (e: Exception) {
            logger.error(e.stackTraceString)
        }
    }

    fun <T> putOpsListLeft(key: String, value: Any):  Long {
        return redisTemplate!!.opsForList().leftPush(key as Nothing, value as Nothing)
    }

    fun hashExpire(key: String, value: Long, unit: TimeUnit?): Boolean {
        return redisTemplate!!.expire(key as Nothing, value, unit)
    }

    companion object {
        private val logger: KLogger = KotlinLogging.logger(RedisUtil::class.java.name)
        private var redisUtil: RedisUtil? = null
        val instance: RedisUtil?
            get() {
                if (RedisUtil.redisUtil == null) {
                    RedisUtil.redisUtil = RedisUtil()
                }
                return redisUtil
            }
    }
}