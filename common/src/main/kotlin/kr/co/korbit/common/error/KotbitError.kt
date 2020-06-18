package kr.co.korbit.common.error

import com.typesafe.config.ConfigFactory
import kr.co.korbit.common.conf.ApplicationConfig
import kr.co.korbit.common.conf.HoconApplicationConfig
import kr.co.korbit.gia.exception.KorbitException
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

        fun error(code: ErrorCode, vararg args: Any?): KorbitError {
            try {
                val errConf: ApplicationConfig = KorbitError.errorConfig.config("error." + code.name)
                val errMessageConf: ApplicationConfig = KorbitError.errorConfig.config("error." + code.name + ".message")

                val error: KorbitError = KorbitError(
                    code.name,
                    errConf.property("description").getString(),
                    HashMap<String, String>(),
                    errConf.property("origin").getString()
                )
                ErrorMessageLang.values().forEach {
                    if( args.size > 0 ) {
                        error.message.put(it.name, errMessageConf.property(it.name).getString().format(args))
                    } else {
                        error.message.put(it.name, errMessageConf.property(it.name).getString())
                    }

                }
                return error
            }catch(e: Throwable) {
                logger.error( "Env.message : " +  e.stackTrace)
                throw e
            }
        }

        fun error(kex: KorbitException): KorbitError? {
            val error: KorbitError? = error(kex.code, kex.argList.toArray())
            error?.let {
                kex.cause?.let {
                    error.description = it.localizedMessage
                }
            }
            return error
        }
    }
}

enum class ErrorCode {
    E00000,
    E00002,
    E00003,
    E10001,
    E10002,
}

enum class ErrorMessageLang {
    ko,
    en
}