package kr.co.korbit.gia.exception.handler

import kr.co.korbit.common.error.ErrorCode
import kr.co.korbit.common.error.KorbitError
import kr.co.korbit.common.extensions.stackTraceString
import kr.co.korbit.gia.exception.*
import mu.KotlinLogging
import org.springframework.beans.ConversionNotSupportedException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.async.AsyncRequestTimeoutException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.BindException
import javax.servlet.http.HttpServletRequest

val logger = KotlinLogging.logger(ExceptionHandler::class.java.name)

@RestControllerAdvice("kr.co.korbit.gia.controller.internal", "kr.co.korbit.gia.controller.external", "kr.co.korbit.gia.controller.public")
@Order(Ordered.HIGHEST_PRECEDENCE)
class ExceptionHandler : ResponseEntityExceptionHandler() {
    //========================================================================================================
    //  Handle intentional exceptions
    //========================================================================================================
    @ExceptionHandler(Exception::class)
    fun handleKorbitException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        if( ex is KorbitException )
            return handleExceptionInternal(ex, null, HttpHeaders(), HttpStatus.OK, request)
        else
            return handleExceptionInternal(ex, null, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request)
    }
    
    //========================================================================================================
    //  Handle exceptions finally
    //========================================================================================================
    override fun handleExceptionInternal(
        ex: java.lang.Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {

        lateinit var err:KorbitError
        when(ex) {
            is KorbitException -> {
                err = KorbitError.error(ex)
            }
            else -> {

                err = KorbitError.error(ErrorCode.E00000)
                ex.message?.let {
                    err.description = it
                }

                logger.error(ex.stackTraceString)
            }
        }


        return super.handleExceptionInternal(ex, err, HttpHeaders(), status, request)
    }
}