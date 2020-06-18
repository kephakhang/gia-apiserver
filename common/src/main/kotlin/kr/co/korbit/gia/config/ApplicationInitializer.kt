package kr.co.korbit.gia.config

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer


open class ApplicationInitializer : AbstractAnnotationConfigDispatcherServletInitializer() {
    override fun getRootConfigClasses(): Array<Class<*>> {
        return arrayOf(WebMvcConfig::class.java)
    }

    override fun getServletConfigClasses(): Array<Class<*>>? {
        return null
    }

    override fun getServletMappings(): Array<String> {
        return arrayOf("/")
    }
}