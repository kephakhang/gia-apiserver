package kr.co.korbit.gia.config

import com.reactor.configuration.swagger.DocumentationPluginsManagerReplaced
import com.reactor.configuration.swagger.TypeNameExtractorReplaced
import kr.co.korbit.gia.config.swagger.DocumentationPluginsManagerReplaced
import kr.co.korbit.gia.config.swagger.TypeNameExtractorReplaced
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import springfox.documentation.schema.TypeNameExtractor
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager


@Configuration
class OverrideSwaggerClasses {
    @Autowired
    private val typeNameExtractorReplaced: TypeNameExtractorReplaced? = null

    @Bean
    @Primary
    fun bean(): DocumentationPluginsManager {
        return DocumentationPluginsManagerReplaced()
    }

    @Bean
    @Primary
    fun beanTypeName(): TypeNameExtractor? {
        return typeNameExtractorReplaced
    }
}