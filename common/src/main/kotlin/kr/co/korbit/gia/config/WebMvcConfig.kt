package kr.co.korbit.gia.config



import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import kr.co.korbit.gia.interceptor.SecurityInterceptor
import org.modelmapper.ModelMapper
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.*
import org.springframework.format.FormatterRegistry
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.validation.MessageCodesResolver
import org.springframework.validation.Validator
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.servlet.config.annotation.*
import org.springframework.web.servlet.handler.MappedInterceptor
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Configuration
@EnableWebMvc
@EnableAutoConfiguration
@ComponentScan("kr.co.korbit.gia")
@ImportResource("classpath:/app-context.xml")
class WebMvcConfig(
    val unifiedArgumentResolver: UnifiedArgumentResolver = UnifiedArgumentResolver()
    ) : WebMvcConfigurer {

    private val RESOURCE_LOCATIONS =
        arrayOf(
            "classpath:/static/"
        )

    @Bean
    fun objectMapper(): ObjectMapper? {
        return ObjectMapper()
            .setAnnotationIntrospector(JacksonAnnotationIntrospector())
            .registerModule(JavaTimeModule().addDeserializer(
                LocalDateTime::class.java,
                LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
            ))
            .setDateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"))
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)

    }

    @Bean
    fun securityInterceptor(): MappedInterceptor {
        return MappedInterceptor(
            arrayOf("/**"), arrayOf("/resource/**"),
            SecurityInterceptor()
        )
    }


    @Bean
    fun modelMapper(): ModelMapper {
        return ModelMapper()
    }

    @Bean
    fun responseBodyConverter(): HttpMessageConverter<String> {
        return StringHttpMessageConverter(Charset.forName("UTF-8"))
    }


//    @Bean
//    fun loggingFilter(): javax.servlet.Filter {
//        val filter: LoggingFilter = LoggingFilter()
//        return filter
//    }

    @Bean
    fun webServerFactoryCustomizer(): WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
        return WebServerFactoryCustomizer { factory -> factory.setContextPath("") }
    }

    /*
     * spring-boot locale 변경 인터셉터
     *
     * @return LocaleChangeInterceptor
     */
    @Bean
    fun localeChangeInterceptor(): LocaleChangeInterceptor {
        val localeChangeInterceptor = LocaleChangeInterceptor()
        // request로 넘어오는 language parameter를 받아서 locale로 설정 한다.
        localeChangeInterceptor.paramName = "language"
        return localeChangeInterceptor
    }

    @Bean(name = ["localeResolver"])
    fun sessionLocaleResolver(): SessionLocaleResolver {
        // 세션 기준으로 로케일을 설정 한다.
        val localeResolver = SessionLocaleResolver()
        // 쿠키 기준(세션이 끊겨도 브라우져에 설정된 쿠키 기준으로)
        // CookieLocaleResolver localeResolver = new CookieLocaleResolver();

        // 최초 기본 로케일을 강제로 설정이 가능 하다.
        localeResolver.setDefaultLocale(Locale.getDefault())
        return localeResolver
    }

    override fun configurePathMatch(pathMatchConfigurer: PathMatchConfigurer) {}
    override fun configureContentNegotiation(contentNegotiationConfigurer: ContentNegotiationConfigurer) {}
    override fun configureAsyncSupport(asyncSupportConfigurer: AsyncSupportConfigurer) {}
    override fun configureDefaultServletHandling(defaultServletHandlerConfigurer: DefaultServletHandlerConfigurer) {}
    override fun addFormatters(formatterRegistry: FormatterRegistry) {}
    override fun addInterceptors(interceptorRegistry: InterceptorRegistry) {}
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {

        registry.addResourceHandler("/**")
            .addResourceLocations(RESOURCE_LOCATIONS[0])

        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

    override fun addCorsMappings(corsRegistry: CorsRegistry) {}
    override fun addViewControllers(viewControllerRegistry: ViewControllerRegistry) {}
    override fun configureViewResolvers(viewResolverRegistry: ViewResolverRegistry) {}

//    val unifiedArgumentResolver: UnifiedArgumentResolver = context.getBean(UnifiedArgumentResolver::class.java)

    override fun addArgumentResolvers(list: MutableList<HandlerMethodArgumentResolver>) {
        list.add(unifiedArgumentResolver)
    }

    override fun addReturnValueHandlers(list: List<HandlerMethodReturnValueHandler>) {}
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {

        val jacksonMessageConverter = MappingJackson2HttpMessageConverter()
        jacksonMessageConverter.objectMapper.setAnnotationIntrospector(JacksonAnnotationIntrospector())
            .registerModule(JavaTimeModule().addDeserializer(
                LocalDateTime::class.java,
                LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
            ))
            .setDateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"))
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            converters.add(jacksonMessageConverter)
    }

    override fun getValidator(): Validator? {
        return null
    }

    override fun getMessageCodesResolver(): MessageCodesResolver? {
        return null
    }
}