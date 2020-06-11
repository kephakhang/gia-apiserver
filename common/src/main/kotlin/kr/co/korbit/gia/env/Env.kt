@file:Suppress("NAME_SHADOWING")
package kr.co.korbit.gia.env

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.typesafe.config.ConfigFactory
import kr.co.korbit.common.conf.HoconApplicationConfig
import kr.co.korbit.gia.jpa.common.UserStatus
import kr.co.korbit.gia.jpa.test.model.Session
import mu.KotlinLogging
import org.apache.kafka.common.protocol.types.ArrayOf
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ConcurrentHashMap


private val logger = KotlinLogging.logger(Env::class.java.name)

class Env {
    companion object {
        val password = "vmffotvhaxla"
        val korbit = "korbit"
        val connectionsLimit = 10
        val lang = Locale.getDefault().language
        val topicConsumeCounts: ConcurrentHashMap<String, Int> = ConcurrentHashMap<String, Int>()
        var topicLogging: Boolean = false
        val topicLogRollingPeriod: Long = 7L
        var branch = "local"

        val appConfig = HoconApplicationConfig(ConfigFactory.load("application.conf"))
        val greeting: String = "HELLO This is Kobit's API server!"
        val normalClosureMessage: String = "Normal closure"

        var objectMapper: ObjectMapper = ObjectMapper()

        init {
            val javaTimeModule = JavaTimeModule()
            // Hack time module to allow 'Z' at the end of string (i.e. javascript json's)
            javaTimeModule.addDeserializer(
                LocalDateTime::class.java,
                LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
            )
            objectMapper.registerModule(javaTimeModule)
            //objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
            objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            objectMapper.dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        }

        fun getTestSession(): Session {
            return Session(
                "3c9aa398-0004-4324-a826-8c7fdaae633c",
                null,
                "5d0ad68d5caa68e4b8dcbd1fd3e5621f1f908426edd8f04ff7e4f0bf04645738",
                "$2a$13$4NNAyWx81NJX2w3Z1j0YoOT9/HizPH1UR354/i11CunXDmAKeKB/a",
                null,
                null,
                null,
                null,
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                LocalDateTime.parse("2020-04-23T08:28:26.000", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                LocalDate.parse("1969-01-01", DateTimeFormatter.ISO_DATE),
                "m",
                "0",
                "KR",
                null,
                true,
                null,
                null,
                null,
                null,
                LocalDateTime.parse("2020-04-23T08:31:55.000", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                "verification_success",
                "KORBIT20200423083011744",
                23177,
                LocalDateTime.parse("2020-04-23T08:31:55.000", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")),
                null,
                "",
                UserStatus.registered,
                1L,
                true,
                true,
                false
            )
        }

        fun isValidCoinInfoJson(input: String): Boolean {
            return input.replace("[0-9a-zA-Z_\\s\"{},\\.\\:\\[\\]]+".toRegex(),"").isEmpty()
        }


        @JvmStatic fun main(argv: List<String>) {
            System.out.println(LocalDateTime.parse("2020-04-23T08:28:26.000", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")).toString())
        }

    }
}

