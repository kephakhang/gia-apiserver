package kr.co.korbit.gia.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * Swagger 설정
 * [공식문서 링크](https://springfox.github.io/springfox/docs/current/)
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun productApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("kr.co.korbit.gia"))
            .build()
            .apiInfo(this.metaInfo())
    }

    /**
     * 문서 상단의 메타정보를 보여준다.
     */
    private fun metaInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("Korbit Gia API 문서")
            .description("테스트 API 문서입니다.")
            .version("1.0")
            .termsOfServiceUrl("https://korbit.co.kr")
            .license("Korbit")
            .licenseUrl("http://license.com")
            .contact(
                Contact(
                    "gia",
                    "https://korbit.co.kr",
                    "kepha@korbit.co.kr"
                )
            )
            .build()
    }
}
