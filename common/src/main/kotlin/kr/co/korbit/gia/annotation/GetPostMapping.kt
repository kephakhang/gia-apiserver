package kr.co.korbit.gia.annotation

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = [RequestMethod.POST, RequestMethod.GET])
annotation class GetPostMapping(vararg val value: String) 