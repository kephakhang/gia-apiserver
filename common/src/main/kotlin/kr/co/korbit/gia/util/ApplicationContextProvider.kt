package kr.co.korbit.gia.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import java.util.*

@Component
class ApplicationContextProvider : ApplicationContextAware {
    @Throws(BeansException::class)
    override fun setApplicationContext(context: ApplicationContext) {
        ApplicationContextProvider.context = context
    }

    companion object {
        var context: ApplicationContext? = null
        var objectMapper: ObjectMapper? = null
            fun get(): ObjectMapper? {
                if (objectMapper == null) {
                    val a = context!!.getBean(RequestMappingHandlerAdapter::class.java)
                    val c = a.messageConverters
                    for (item in c) {
                        if (item is MappingJackson2HttpMessageConverter) {
                            objectMapper =
                                item.objectMapper
                            break
                        }
                    }
                }
                return objectMapper
            }

        fun <T> getBean(clz: Class<T>?): T {
            return ApplicationContextProvider.context!!.getBean(clz)
        }

        fun getBean(beanName: String?): Any {
            return ApplicationContextProvider.context!!.getBean(beanName)
        }

        fun getConfigValue(key: Any?): Any? {
            val config = getBean(
                Properties::class.java
            ) ?: return null
            return config[key]
        }

        fun getConfigValue(key: String?): String? {
            val config = getBean(
                Properties::class.java
            ) ?: return null
            return config.getProperty(key)
        }
    }
}