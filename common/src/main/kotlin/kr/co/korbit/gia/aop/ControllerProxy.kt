package kr.co.korbit.gia.aop

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.korbit.common.error.ErrorCode
import kr.co.korbit.common.error.KorbitError
import kr.co.korbit.common.extensions.stackTraceString
import kr.co.korbit.gia.annotation.SkipSessionCheck
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.exception.SessionNotFoundException
import kr.co.korbit.gia.jpa.common.Response
import kr.co.korbit.gia.jpa.test.model.Session
import kr.co.korbit.gia.util.KeyGenerator
import kr.co.korbit.gia.util.RequestWrapper
import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest


private val logger = KotlinLogging.logger(ControllerProxy::class.java.name)

@Aspect
@Configuration
class ControllerProxy {
//    val gson: Gson = GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create()
    val CRLF = "\n"
    val REQUEST_PREFIX = "Request: "
    val RESPONSE_PREFIX = "Response: "

    @Autowired
    lateinit var context: ApplicationContext

    @Throws(Throwable::class)
    fun checkOAuth2(call: ProceedingJoinPoint, req: HttpServletRequest, method: Method) {

        /*MGK_ADD : OAuth2 인증 체크 부분 추가 [
        if (Env.oauth2Enable) {
            try {
                tokenRepository = context!!.getBean(TokenRepository::class.java)
                // 애플리케이션에서 Request 객체를 읽어옴
                val req =
                    (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
                var authorization = req.getHeader("Authorization")
                if (authorization == null) {
                    authorization = req.getHeader("authorization")
                    if (authorization == null) {
                        throw InvalidOauth2TokenException("oauth2 error", "Authorization header is needed")
                    }
                }
                val dateStr: String = AppUtil.localDatetimeNowString()
                val accessToken = authorization.replaceFirst("Bearer ".toRegex(), "")
                val token: Token = tokenRepository.findByToken(accessToken)
                if (token == null || token.getExpireDate().compareTo(dateStr) < 1) {
                    throw InvalidOauth2TokenException("oauth2 error", "Authorization header is not valid")
                }
            } catch (ex: ResultCodeException) {
                ex.put("requestId", requestId)
                return ex
            }
        }
        // ]
        */

    }

    private fun isHtml(requestUri: String): Boolean {
        return try {
            if ( "/(.+\\.(ico|js|css|html|jpg|jpeg|png|gif|svg))".toRegex().containsMatchIn(requestUri) ) true else false
        } catch (ex: Exception) {
            false
        }
    }

    private fun isMultipart(request: HttpServletRequest): Boolean {
        return (request.contentType != null
                && request.contentType.startsWith("multipart/form-data"))
    }

    private fun isFormSubmit(request: HttpServletRequest): Boolean {
        return (request.contentType != null
                && request.contentType.startsWith(
            "application/x-www-form-urlencoded"
        ))
    }

    fun loggging(
        startTime: Long,
        requestId: String,
        requestUri: String,
        isException: Boolean,
        req: HttpServletRequest,
        response: Any?
    ) {
        val logging = true
        val msg = StringBuilder()
        if (!isException) {

            msg.append(CRLF).append("################################").append("--:" + requestId + ":--[")
                .append(CRLF).append(REQUEST_PREFIX)
                .append(requestUri).append(":")

        } else {
            msg.append(CRLF).append("###Exception####################").append("--:" + requestId + ":--[")
                .append(CRLF).append(REQUEST_PREFIX)
                .append(requestUri).append(":")
        }
        if (req.queryString != null
            && !req.queryString.isEmpty()
        ) msg.append('?').append(req.queryString)
        msg.append(" ").append(req.method)
        val session = req.getSession(false)
        if (session != null) {
            msg.append(CRLF).append("SESSION ID: ").append(session.id)
        }

        msg.append(CRLF).append("authKey: ").append(MDC.get("authKey")).append(CRLF)

        var ip: String? = req.getHeader("X-FORWARDED-FOR")
        if (ip == null) ip = req.getRemoteAddr()

        ip?.let {
            msg.append(CRLF).append("Remote-IP: ")
                .append(ip).append(CRLF)
        }

        msg.append("Content-Type: ").append(req.contentType)
        if (!isMultipart(req)) {
            msg.append(CRLF).append("Request Body: ").append(requestUri).append(":").append(CRLF)
            if (isFormSubmit(req)) {
                var firstParam = true
                val e: Enumeration<*> = req.parameterNames
                while (e
                        .hasMoreElements()
                ) {
                    val pn = e.nextElement() as String
                    val pv = req.getParameterValues(pn)
                    for (l in pv.indices) {
                        if (firstParam) {
                            msg.append(pn + "=" + pv[l])
                            firstParam = false
                        } else {
                            msg.append("&").append(pn).append("=")
                                .append(pv[l])
                        }
                    }
                }
            } else {
                msg.append(String(RequestWrapper(req).toByteArray()))
            }
        }
        msg.append(CRLF).append("Elapsed Time: ")
            .append(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime))
            .append(" msec")

        if (response is Response) {
            msg.append(CRLF).append("-------------------------------")
                .append(CRLF).append(RESPONSE_PREFIX)
            msg.append(CRLF).append(Env.objectMapper.writeValueAsString(response))
        }
        msg.append(CRLF).append("################################").append("--:" + requestId + ":--]")
        if (isException) {
            logger.error(
                msg.toString().replace("\\t".toRegex(), "\t").replace("\\n".toRegex(), "\n")
            )
        } else {
            logger.debug(
                if (msg.length > 20480) msg.substring(0, 20480)
                        + "..... (more data. len:" + msg.length + ")" else msg
                    .toString().replace("\\t".toRegex(), "\t").replace("\\n".toRegex(), "\n")
            )
        }
    }

    @Around("execution(* kr.co.korbit.gia.controller.internal.*.*(..)) || execution(* kr.co.korbit.gia.controller.public.*.*(..)) || execution(* kr.co.korbit.gia.controller.admin.*.*(..))")
    fun around(call: ProceedingJoinPoint): Any? {
        var response: Any? = null
        val sig = call.signature as MethodSignature
        val method = sig.method
        val args = call.args
        val req: HttpServletRequest =
            (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val requestId: String = KeyGenerator.generateOrderNo()
        val requestUri: String = req.requestURI
        val startTime = System.nanoTime()
        var isException = false


        try {
            logger.debug("around AOP in : $args")

            // session 값을 필요한 경우 Controller 의 RestApi 함수 파라미터로 자동 injection
            var session: Session? = null
            if( !method.isAnnotationPresent(SkipSessionCheck::class.java) ) {
                val genericParameterTypes = method.genericParameterTypes
                val returnType = method.returnType
                val i = 0
                for (genericParameterType in genericParameterTypes) {
                    if (genericParameterType is ParameterizedType) {
                        continue
                    } else {
                        val cls = genericParameterType as Class<*>
                        if (Session::class.java.isAssignableFrom(cls)) {
                            session = args[i] as Session
                            break
                        }
                    }
                }

                if( session == null ) {
                    throw SessionNotFoundException(Exception("SessionNotFound"))
                }
            }


            val res: Any? = call.proceed(args)
            when( res ) {
                is ErrorCode -> {
                    isException = true
                    val err = KorbitError.error(res)
                    response = Response(false, err, MDC.get("requestId"), MDC.get("requestUri"), req.method.toUpperCase())
                }
                is KorbitError -> {
                    isException = true
                    response = Response(false, res, MDC.get("requestId"), MDC.get("requestUri"), req.method.toUpperCase())
                }
                is Throwable -> {
                    logger.error(res.stackTraceString)
                    isException = true
                    val err = KorbitError.error(ErrorCode.E00000)
                    err.description = res.localizedMessage
                    response = Response(false, err as Any, MDC.get("requestId"), MDC.get("requestUri"), req.method.toUpperCase())
                }
                is Response -> {
                    if( res.body is Exception )
                        isException = true
                    response = Response(res.success, res.body, MDC.get("requestId"), MDC.get("requestUri"), req.method.toUpperCase())
                }
                else -> {
                    response = res
                }
            }
        } catch(ex: SessionNotFoundException) {
            logger.error("proceed error return", ex.stackTraceString)
            isException = true
            val err = KorbitError.error(ErrorCode.E00003)
            response = Response(false, err as Any, MDC.get("requestId"), MDC.get("requestUri"), req.method.toUpperCase())
        } catch (ex: Throwable) {
            logger.error("proceed error return", ex.stackTraceString)
            isException = true
            val err = KorbitError.error(ErrorCode.E00000)
            err.description = ex.localizedMessage
            response = Response(false, err as Any, MDC.get("requestId"), MDC.get("requestUri"), req.method.toUpperCase())
        } finally {
            if (logger.isDebugEnabled) {
                if (response != null) {
                    loggging(  startTime,
                                requestId,
                                requestUri,
                                isException,
                                req,
                                response)
                }
            }
            return response
        }
    }
}