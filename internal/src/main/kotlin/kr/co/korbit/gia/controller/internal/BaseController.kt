package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.config.WebMvcConfig
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.stereotype.Controller

open class BaseController  {

    companion object {
        public const val baseUri = "/internal/v1"
    }
}