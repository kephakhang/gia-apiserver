package kr.co.korbit.gia.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.util.ReferenceSerializationConfigurer

import springfox.documentation.spring.web.json.JacksonModuleRegistrar

// ref : https://github.com/springfox/springfox/issues/2917
class SwaggerJacksonModuleRegistrar : JacksonModuleRegistrar {
    override fun maybeRegisterModule(objectMapper: ObjectMapper) {
        ReferenceSerializationConfigurer.serializeAsComputedRef(objectMapper)
    }
}