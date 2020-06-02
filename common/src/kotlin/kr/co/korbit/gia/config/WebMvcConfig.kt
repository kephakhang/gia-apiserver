package kr.co.korbit.gia.config


import kr.co.korbit.gia.yml.YamlPropertySourceFactory
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.*
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.io.ClassPathResource
import org.springframework.format.FormatterRegistry
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.validation.MessageCodesResolver
import org.springframework.validation.Validator
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.servlet.config.annotation.*
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*


@Configuration
@EnableWebMvc
@EnableAutoConfiguration
@ComponentScan("kr.co.korbit.gia.controller")
//@PropertySource(value = ["classpath:/application.yml"], ignoreResourceNotFound = true, factory = YamlPropertySourceFactory::class)
@ImportResource("classpath:/app-context.xml")
class WebMvcConfig : WebMvcConfigurer {

    companion object {
        private val RESOURCE_LOCATIONS =
            arrayOf( /*   "classpath:/META-INF/resources/",  "classpath:/resources/", */
                "classpath:/static/"
            )
    }

    @Bean
    fun modelMapper(): ModelMapper {
        return ModelMapper()
    }

    //ToDo UnifiedArgumentResolver 세팅 필요
//    @Autowired
//    var unifiedArgumentResolver: UnifiedArgumentResolver? = null

//    @Bean
//    fun  properties(): PropertySourcesPlaceholderConfigurer? {
//        val propertySourcesPlaceholderConfigurer: PropertySourcesPlaceholderConfigurer = PropertySourcesPlaceholderConfigurer()
//        var yaml: YamlPropertiesFactoryBean = YamlPropertiesFactoryBean()
//        yaml.setResources(ClassPathResource ("application.yml"))
//        yaml.getObject()?.let {
//            propertySourcesPlaceholderConfigurer.setProperties(it)
//            return propertySourcesPlaceholderConfigurer
//        }
//        return null
//    }

    @Bean
    fun responseBodyConverter(): HttpMessageConverter<String> {
        return StringHttpMessageConverter(Charset.forName("UTF-8"))
    }


//    @Bean
//    fun characterEncodingFilter(): javax.servlet.Filter {
//        val filter: CharacterEncodingFilter = CharacterEncodingFilter()
//        filter.encoding = "UTF-8"
//        filter.setForceEncoding(true)
//        return filter
//    }

    @Bean
    fun webServerFactoryCustomizer(): WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
        return WebServerFactoryCustomizer { factory -> factory.setContextPath("/gia") }
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
        localeResolver.setDefaultLocale(Locale("ko"))
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
            .addResourceLocations(WebMvcConfig.RESOURCE_LOCATIONS[0])

    }

    override fun addCorsMappings(corsRegistry: CorsRegistry) {}
    override fun addViewControllers(viewControllerRegistry: ViewControllerRegistry) {}
    override fun configureViewResolvers(viewResolverRegistry: ViewResolverRegistry) {}
    //ToDo 나중에 세팅 필요
//    override fun addArgumentResolvers(list: MutableList<HandlerMethodArgumentResolver>) {
//        list.add(unifiedArgumentResolver)
//    }

    override fun addReturnValueHandlers(list: List<HandlerMethodReturnValueHandler>) {}
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        val builder = Jackson2ObjectMapperBuilder()
        builder.indentOutput(true).dateFormat(SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
        converters.add(MappingJackson2HttpMessageConverter(builder.build()))
    }

    override fun getValidator(): Validator? {
        return null
    }

    override fun getMessageCodesResolver(): MessageCodesResolver? {
        return null
    }


}