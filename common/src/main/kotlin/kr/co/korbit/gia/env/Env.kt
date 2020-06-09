@file:Suppress("NAME_SHADOWING")
package kr.co.korbit.gia.env

import com.typesafe.config.ConfigFactory
import kr.co.korbit.common.conf.ApplicationConfigValue
import kr.co.korbit.common.conf.HoconApplicationConfig
import kr.co.korbit.gia.jpa.common.UserStatus
import kr.co.korbit.gia.jpa.test.model.Session
import mu.KotlinLogging
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger {}

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

        fun getTestSession(): Session {
            return Session(
                "3c9aa398-0004-4324-a826-8c7fdaae633c",
                null,
                "5d0ad68d5caa68e4b8dcbd1fd3e5621f1f908426edd8f04ff7e4f0bf04645738",
                "$2a$13$4NNAyWx81NJX2w3Z1j0YoOT9/HizPH1UR354/i11CunXDmAKeKB/a",
                null,
                null,
                null, 0,
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
                LocalDateTime.parse("2020-04-23 08:28:26", DateTimeFormatter.ISO_DATE_TIME),
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
                LocalDateTime.parse("2020-04-23 08:31:55", DateTimeFormatter.ISO_DATE_TIME),
                "verification_success",
                "KORBIT20200423083011744",
                23177,
                LocalDateTime.parse("2020-04-23 08:31:55", DateTimeFormatter.ISO_DATE_TIME),
                null,
                "",
                UserStatus.registered,
                1L,
                false,
                true,
                true
            )
        }

        fun isValidCoinInfoJson(input: String): Boolean {
            return input.replace("[0-9a-zA-Z_\\s\"{},\\.\\:\\[\\]]+".toRegex(),"").isEmpty()
        }
    }
}
