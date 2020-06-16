package kr.co.korbit.gia.annotation

import kotlin.annotation.Retention

@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention
annotation class SkipSessionCheck 