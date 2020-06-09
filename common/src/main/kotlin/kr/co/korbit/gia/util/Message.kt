package kr.co.korbit.gia.util


import com.typesafe.config.ConfigFactory
import kr.co.korbit.common.conf.ApplicationConfigValue
import kr.co.korbit.common.conf.HoconApplicationConfig
import kr.co.korbit.common.extensions.stackTraceString
import kr.co.korbit.gia.env.Env
import mu.KotlinLogging
import java.util.*



class Message {
    companion object {

        val logger = KotlinLogging.logger {}

        val lang = Locale.getDefault().language
        val messageConfig = HoconApplicationConfig(ConfigFactory.load("i18n/" + lang))

        fun messageProp(key: String): ApplicationConfigValue? {
            try {
                val idx: Int = key.lastIndexOf(".")
                val group: String = key.substring(0, idx)
                val key: String = key.substring(idx + 1)
                return messageConfig.config(group).property(key)
            }catch(e: Throwable) {
                logger.error( "Env.message : " +  e.stackTraceString)
                return null
            }
        }

        fun message(key: String): String {
            try {
                val idx: Int = key.lastIndexOf(".")
                val group: String = key.substring(0, idx)
                val property: String = key.substring(idx + 1)
                logger.debug("Evn.message - group : " + group)
                logger.debug("Evn.message - key : " + property)
                return Message.messageConfig.config(group).property(property).getString()
            }catch(e: Throwable) {
                logger.error( "Env.message : " +  e.stackTraceString)
                return ""
            }
        }
    }
}
