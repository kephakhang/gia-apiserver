package kr.co.korbit.gia.exception.handler

import kr.co.korbit.common.error.ErrorCode
import kr.co.korbit.common.error.KorbitError
import kr.co.korbit.gia.exception.*
import kr.co.korbit.gia.jpa.common.Response
import mu.KotlinLogging
import org.hibernate.exception.ConstraintViolationException
import org.slf4j.MDC
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.MultipartException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

val logger = KotlinLogging.logger(RestResponseEntityExceptionHandler::class.java.name)

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    //========================================================================================================
    //  Handle intentional exceptions
    //========================================================================================================
    @ExceptionHandler(InternalException::class)
    fun handleInternalException(ex: InternalException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, HttpStatus.OK, ex.localizedMessage, request)
    }
    //========================================================================================================
    //  Handle bad request exceptions
    //========================================================================================================
    /**
     * Validate request message syntax.
     */
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        if (ex.message!!.contains("Required request body is missing")) {
            val message = "Required request body is missing."
            return handleExceptionInternal(
                ex,
                HttpStatus.UNPROCESSABLE_ENTITY,
                message,
                request
            )
        }
        val message = "The request could not be understood by the server due to malformed syntax."
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, message, request)
    }

    override fun handleMissingServletRequestPart(
        ex: MissingServletRequestPartException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, ex.message, request)
    }

    override fun handleTypeMismatch(
        ex: TypeMismatchException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return handleExceptionInternal(ex, HttpStatus.NOT_ACCEPTABLE, ex.message, request)
    }

    @ExceptionHandler(MultipartException::class)
    protected fun handleMultipartException(ex: MultipartException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, HttpStatus.NOT_ACCEPTABLE,  ex.message, request)
    }

    /**
     * Conversion service error.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun handleConverterException(
        ex: MethodArgumentTypeMismatchException,
        request: WebRequest
    ): ResponseEntity<Any> {
        return handleExceptionInternal(ex, HttpStatus.NOT_ACCEPTABLE,  ex.message, request)
    }
    //========================================================================================================
    //  Handle validation exceptions
    //========================================================================================================
    /**
     * Validate required request parameter that it is missing or not.
     */
    public override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
//        return handleExceptionInternal(ex, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.INVALID_REQUEST, ex.getMessage(), request);
        return handleExceptionInternal(ex, HttpStatus.OK,  ex.message, request)
    }

    /**
     * Validate request parameter by @Valid.
     */
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        httpStatus: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        var message: String = ""
        val result = ex.bindingResult
        for (error in result.allErrors) {
            if (message.isNotBlank()) {
                message += " "
            }
            message += error.defaultMessage
        }

//        return handleExceptionInternal(ex, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.INVALID_REQUEST, message, request);
        return handleExceptionInternal(ex, HttpStatus.OK, message, request)
    }

    /**
     * Validate request parameter by custom validation annotation.
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: WebRequest
    ): ResponseEntity<Any> {
        var message: String = ""
        for (violation in ex.constraintName) {
            if (message.isNotBlank()) {
                message += " "
            }
            message += violation.toString()
        }

//        return handleExceptionInternal(ex, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.INVALID_REQUEST, message, request);
        //return handleExceptionInternal(ex, HttpStatus.OK, ErrorCode.invalid_request, message, request)
        return handleExceptionInternal(ex, HttpStatus.OK, message, request)
    }

    /**
     * Validate multipart maxUploadSize.
     */
    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSizeExceededException(
        ex: MaxUploadSizeExceededException,
        request: WebRequest
    ): ResponseEntity<Any> {
        return handleExceptionInternal(
            ex,
            HttpStatus.UNPROCESSABLE_ENTITY,
            ex.message,
            request
        )
    }

    /**
     * Validate request parameter by programmatically.
     */
    @ExceptionHandler(
        NullPointerException::class,
        IllegalArgumentException::class
    )
    protected fun handlePreConditionsException(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        if (ex is NullPointerException || ex is IllegalArgumentException) {
            if (ex.message != null) {
//                return handleExceptionInternal(ex, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.INVALID_REQUEST, ex.getMessage(), request);
                return handleExceptionInternal(ex, HttpStatus.OK, ex.message, request)
            }
        }
        return handleBaseException(ex, request)
    }

    //========================================================================================================
    //  Handle security exceptions
    //========================================================================================================
    /*
    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException, request: WebRequest?): ResponseEntity<Any> {
        return if (ex is BadCredentialsException) {
            handleExceptionInternal(
                ex,
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request
            )
        } else handleExceptionInternal(
            ex,
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            request
        )
    }
    */


    @ExceptionHandler(AccessDeniedException::class)
    protected fun handleAccessDeniedException(
        ex: AccessDeniedException,
        request: WebRequest
    ): ResponseEntity<Any> {
        return handleExceptionInternal(ex, HttpStatus.FORBIDDEN,  ex.localizedMessage, request)
    }

    @ExceptionHandler(UnsupportedOperationException::class)
    protected fun handleUnsupportedOperationException(
        ex: UnsupportedOperationException,
        request: WebRequest
    ): ResponseEntity<Any> {
        return handleExceptionInternal(ex, HttpStatus.FORBIDDEN,  ex.localizedMessage, request)
    }

    //========================================================================================================
    //  Handle base exceptions
    //========================================================================================================
    @ExceptionHandler(Exception::class)
    fun handleBaseException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(
            ex,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Server failed to complete the request due to unexpected error.",
            request
        )
    }

    //========================================================================================================
    //  Handle exceptions finally
    //========================================================================================================
    @Throws(Throwable::class)
    private fun handleExceptionInternal(
        ex: Exception,
        status: HttpStatus,
        message: String?,
        request: WebRequest
    ): ResponseEntity<Any> {

        val req: HttpServletRequest =
            (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val uri = req.requestURI
        val method = req.method.toUpperCase()
        var err: KorbitError = KorbitError.error(ErrorCode.E00000)

        when(ex) {
            is IllegalStateException  -> {

                when(uri) {
                    "/v1/user" -> {
                        when(method) {
                            "GET" -> {

                                err = KorbitError.error(ErrorCode.E10001, (ex as InternalException).argList.toArray())
                            }
                            "POST" -> {

                            }
                            "PUT" -> {

                            }
                            "DELETE" -> {

                            }
                        }
                    }
                }

            }
            is NotFoundException -> {

            }
            is TooManyRequestException -> {

            }
            is WrongFormatException -> {

            }
            is InternalException -> {

            }
            is SessionNotFoundException -> {
                err = KorbitError.error(ErrorCode.E00003)
            }
            else -> {
//                ex.message?.let {
//                    err.description = it
//                }

                throw ex
            }
        }
        return handleExceptionInternal(ex, Response(false, err as Any, MDC.get("requestId"), uri, method), HttpHeaders(), status, request)
    }

    override fun handleExceptionInternal(
        ex: java.lang.Exception,
        body: Any,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error{
            """
                Exception occurred: $ex
                $request
                HttpStatus: ${status.value()} ${status.reasonPhrase}
                Response: $ex
                StackTrace:
                """.trimIndent()
        }
        return super.handleExceptionInternal(ex, body, headers, status, request)
    }
}