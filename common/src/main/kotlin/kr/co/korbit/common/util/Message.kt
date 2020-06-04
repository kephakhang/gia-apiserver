package kr.co.korbit.common.util


import com.typesafe.config.ConfigFactory
import kr.co.korbit.common.config.ApplicationConfig
import kr.co.korbit.common.config.HoconApplicationConfig
import mu.KotlinLogging
import java.util.*
import kotlin.collections.HashMap

val logger = KotlinLogging.logger {}

class Message {
    companion object {

        val lang = Locale.getDefault().language
        val messageConfig = HoconApplicationConfig(ConfigFactory.load("i18n/" + lang))

        fun message(key: String): String {
            try {
                val idx: Int = key.lastIndexOf(".")
                val group: String = key.substring(0, idx)
                val property: String = key.substring(idx + 1)
                logger.debug("Evn.message - group : " + group)
                logger.debug("Evn.message - key : " + property)
                return Message.messageConfig.config(group).property(property).getString()
            }catch(e: Throwable) {
                logger.error( "Env.message : " +  e.stackTrace)
                return ""
            }
        }
    }
}
