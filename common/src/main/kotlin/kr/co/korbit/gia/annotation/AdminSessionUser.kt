package kr.co.korbit.gia.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(RetentionPolicy.RUNTIME)
annotation class AdminSessionUser 