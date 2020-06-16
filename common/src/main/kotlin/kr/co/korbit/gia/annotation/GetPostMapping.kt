package kr.co.korbit.gia.annotation

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import kotlin.annotation.Retention

@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention
@RequestMapping(method = [RequestMethod.POST, RequestMethod.GET])
annotation class GetPostMapping(vararg val value: String) 