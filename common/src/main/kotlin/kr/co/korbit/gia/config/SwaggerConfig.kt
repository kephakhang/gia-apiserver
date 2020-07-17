package kr.co.korbit.gia.config

import com.fasterxml.classmate.TypeResolver
import kr.co.korbit.gia.jpa.korbit.model.dto.Session
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver
import springfox.documentation.swagger.web.*


/**
 * Swagger 설정
 * [공식문서 링크](https://springfox.github.io/springfox/docs/snapshot/)
 */
@Configuration
@Import(WebMvcConfig::class)
class SwaggerConfig {

//    @Autowired
//    lateinit var typeResolver: TypeResolver


    fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("Korbit Gia Api Server")
            .description("Korbit Gia Api Server Rest-Api 규약")
            .license("korbit copyright")
            .licenseUrl("https://korbit.co.kr")
            .termsOfServiceUrl("https://api.korbit.co.kr/swagger-ui.html")
            .version("1.0.0")
            .contact(Contact("Kepha Khang", "https://korbit.co.kr", "kepha@korbit.co.kr"))
            .build()
    }

    // ref : https://github.com/springfox/springfox/issues/1443
    // ref : https://github.com/springfox/springfox/issues/2917
    @Bean
    fun swaggerJacksonModuleRegistrar(): SwaggerJacksonModuleRegistrar? {
        return SwaggerJacksonModuleRegistrar()
    }

    @Bean
    fun korbitApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .ignoredParameterTypes(Session::class.java)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.any())
            //.apis(RequestHandlerSelectors.basePackage("kr.co.korbit.gia.controller"))
            .paths(PathSelectors.any())
            .build()

//            .pathMapping("/")
//            .directModelSubstitute(LocalDate::class.java, String::class.java)
//            .genericModelSubstitutes(ResponseEntity::class.java)
//            .alternateTypeRules(
//                newRule(
//                    typeResolver?.resolve(
//                        DeferredResult::class.java,
//                        typeResolver.resolve(ResponseEntity::class.java, WildcardType::class.java)
//                    ),
//                    typeResolver?.resolve(WildcardType::class.java)
//                )
//            )
//            .useDefaultResponseMessages(false)
//            .globalResponseMessage(
//                RequestMethod.GET,
//                listOf(
//                    ResponseMessageBuilder()
//                        .code(500)
//                        .message("500 message")
//                        .responseModel(ModelRef("Error"))
//                        .build()
//                )
//            )
//            .securitySchemes(listOf(apiKey()))
//            .securityContexts(listOf(securityContext()))
//            .enableUrlTemplating(false)
//            .globalOperationParameters(
//                listOf(
//                    ParameterBuilder()
//                        .name("someGlobalParameter")
//                        .description("Description of someGlobalParameter")
//                        .modelRef(ModelRef("string"))
//                        .parameterType("query")
//                        .required(true)
//                        .build()
//                )
//            )
            //.tags(Tag("Korbit Gia Api Service", "All apis relating to Korbit"))
            //.additionalModels(typeResolver.resolve(AdditionalModel::class.java))
    }

//
//    private fun apiKey(): ApiKey {
//        return ApiKey("mykey", "api_key", "header")
//    }
//
//
//    fun defaultAuth(): List<SecurityReference?> {
//        val authorizationScope = AuthorizationScope("global", "accessEverything")
//        val authorizationScopes: Array<AuthorizationScope?> = arrayOfNulls<AuthorizationScope>(1)
//        authorizationScopes[0] = authorizationScope
//        return listOf(
//            SecurityReference("mykey", authorizationScopes)
//        )
//    }
//
//    private fun securityContext(): SecurityContext {
//        return SecurityContext.builder()
//            .securityReferences(defaultAuth())
//            .forPaths(regex("/*"))
//            .build()
//    }
//
//    // ToDo : 테스트를 위한 세션 정보 추후 추가 필요
//    @Bean
//    fun security(): SecurityConfiguration {
//        return SecurityConfigurationBuilder.builder()
//            .clientId("test-app-client-id")
//            .clientSecret("test-app-client-secret")
//            .realm("korbit-app-realm")
//            .appName("korbit-app")
//            .scopeSeparator(",")
//            .additionalQueryStringParams(null)
//            .useBasicAuthenticationWithAccessCodeGrant(false)
//            .enableCsrfSupport(false)
//            .build()
//    }



    @Bean
    fun uiConfig(): UiConfiguration? {
        return UiConfigurationBuilder.builder()
            .deepLinking(true)
            .displayOperationId(false)
            .defaultModelsExpandDepth(1)
            .defaultModelExpandDepth(1)
            .defaultModelRendering(ModelRendering.EXAMPLE)
            .displayRequestDuration(false)
            .docExpansion(DocExpansion.NONE)
            .filter(false)
            .maxDisplayedTags(null)
            .operationsSorter(OperationsSorter.ALPHA)
            .showExtensions(false)
            .tagsSorter(TagsSorter.ALPHA)
            .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
            .validatorUrl(null)
            .build()
    }
}
