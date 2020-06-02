package kr.co.korbit.gia

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kr.co.korbit.gia.config.*
import org.springframework.context.annotation.Import


@SpringBootApplication
@Import(value = [JpaKorbitConfig::class, JpaKorbitApiConfig::class, JpaTerraConfig::class, SwaggerConfig::class, WebMvcConfig::class])
class Application {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            runApplication<Application>(*args)
        }
    }
}

