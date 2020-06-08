package kr.co.korbit.gia.config

import com.typesafe.config.ConfigFactory
import kr.co.korbit.common.conf.HoconApplicationConfig
import java.util.*
import kotlin.collections.HashMap

//ToDo : DB 설정 추가 필요
class AppConfig(
        val code: String,
        val description: String,
        val message: HashMap<String, String>,
        val origin: String) {
    companion object {

        val lang = Locale.getDefault().language
        val messageConfig = HoconApplicationConfig(ConfigFactory.load("i18n/" + lang))
        val errorConfig = HoconApplicationConfig(ConfigFactory.load("i18n/error"))

    }
}