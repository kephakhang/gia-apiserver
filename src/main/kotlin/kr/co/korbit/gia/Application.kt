package kr.co.korbit.gia

import kr.co.korbit.gia.config.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import java.util.*
import javax.annotation.PostConstruct


@SpringBootApplication
@Import(value = [JpaKorbitConfig::class, JpaKorbitApiConfig::class, JpaTerraConfig::class, SwaggerConfig::class, WebMvcConfig::class])
class Application {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
            runApplication<Application>(*args)
        }

        @PostConstruct
        fun started() {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        }
    }
}

