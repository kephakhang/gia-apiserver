package kr.co.korbit.gia.exception.handler

import kr.co.korbit.common.error.ErrorCode
import kr.co.korbit.common.error.KorbitError
import kr.co.korbit.common.extensions.stackTraceString
import kr.co.korbit.gia.exception.*
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

val logger = KotlinLogging.logger(ExceptionHandler::class.java.name)

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    //========================================================================================================
    //  Handle intentional exceptions
    //========================================================================================================
    @ExceptionHandler(KorbitException::class)
    fun handleKorbitException(ex: KorbitException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, null, HttpHeaders(), HttpStatus.OK, request)
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

        val req: HttpServletRequest =
            (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val uri = req.requestURI
        val method = req.method.toUpperCase()
        var errCode: ErrorCode = ErrorCode.E00000
        var err:KorbitError? = null

        when(ex) {
            is KorbitException -> {
                err = KorbitError.error((ex as KorbitException))
            }
            else -> {

                err = KorbitError.error(ErrorCode.E00000)
                ex.message?.let {
                    err.description = it
                }

                logger.error(ex.stackTraceString)
            }
        }

        err?.let {
            return super.handleExceptionInternal(ex, err, HttpHeaders(), status, request)
        }
        return super.handleExceptionInternal(ex, errCode, HttpHeaders(), status, request)
    }
}