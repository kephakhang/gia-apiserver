package kr.co.korbit.common.error

import com.typesafe.config.ConfigFactory
import kr.co.korbit.common.conf.ApplicationConfig
import kr.co.korbit.common.conf.HoconApplicationConfig
import mu.KotlinLogging
import java.util.*
import kotlin.collections.HashMap

val logger = KotlinLogging.logger {}
class KorbitError(
    val code: String,
    var description: String,
    val message: HashMap<String, String>,
    val origin: String) {
    companion object {

        val lang = Locale.getDefault().language
        val messageConfig = HoconApplicationConfig(ConfigFactory.load("i18n/" + lang))
        val errorConfig = HoconApplicationConfig(ConfigFactory.load("i18n/error"))

        fun error(code: String, vararg args:Any?): KorbitError {
            try {
                val errConf: ApplicationConfig = KorbitError.errorConfig.config("error." + code)
                val errMessageConf: ApplicationConfig = KorbitError.errorConfig.config("error." + code + ".message")

                val error: KorbitError = KorbitError(
                    code,
                    errConf.property("description").getString(),
                    HashMap<String, String>(),
                    errConf.property("origin").getString()
                )
                ErrorMessageLang.values().forEach {
                    if( args.size > 0 ) {
                        error.message.put(it.lang, errMessageConf.property(it.lang).getString().format(args))
                    } else {
                        error.message.put(it.lang, errMessageConf.property(it.lang).getString())
                    }

                }

                return error
            }catch(e: Throwable) {
                logger.error( "Env.message : " +  e.stackTrace)
                throw e
            }
        }
    }
}

enum class ErrorCode(val value: String) {
    E0000("E0000"),
    E1001("E1001"),
    E1002("E1002"),
    E1003("E1003")
}

enum class ErrorMessageLang(val lang: String) {
    ko("ko"),
    en("en")
}