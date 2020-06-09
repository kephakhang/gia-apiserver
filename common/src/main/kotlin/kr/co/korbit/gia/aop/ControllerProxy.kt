package kr.co.korbit.gia.aop

import kr.co.korbit.common.error.ErrorCode
import kr.co.korbit.common.error.KorbitError
import kr.co.korbit.common.extensions.stackTraceString
import kr.co.korbit.gia.annotation.AdminSessionUser
import kr.co.korbit.gia.annotation.AgentSessionUser
import kr.co.korbit.gia.annotation.SkipSessionCheck
import kr.co.korbit.gia.exception.SessionNotFoundException
import kr.co.korbit.gia.jpa.common.Response
import java.lang.reflect.Method
import kr.co.korbit.gia.jpa.test.service.AuthService
import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.slf4j.MDC
import javax.servlet.http.HttpServletRequest


private val logger = KotlinLogging.logger(ControllerProxy::class.java.name)

@Aspect
@Configuration
class ControllerProxy {


    @Autowired
    var context: ApplicationContext? = null

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

    @Throws(SessionNotFoundException::class)
    fun checkSession(call: ProceedingJoinPoint, req: HttpServletRequest, method: Method) {
        val req: HttpServletRequest =
            (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        var authKey = req.getHeader("Authorization")
        if (authKey == null || authKey.isEmpty()) {
            req.cookies?.let {
                for (cookie in it) {
                    if (cookie.name.equals("Korbit-Authorization", ignoreCase = true)) {
                        authKey = cookie.value
                        break
                    }
                }
            }
            if (authKey == null || authKey.isEmpty())
                authKey = req.getParameter("Authorization")
        }

        val authService: AuthService = context!!.getBean(AuthService::class.java)

        var sessionMemberId: Long? = null
        if (!method.isAnnotationPresent(SkipSessionCheck::class.java) &&
            !method.isAnnotationPresent(AgentSessionUser::class.java) &&
            !method.isAnnotationPresent(AdminSessionUser::class.java)
        ) { // public api session
            authService.checkAuth(authKey)
        } else if (method.isAnnotationPresent(AgentSessionUser::class.java)) {
            authService.checkAgentAuth(authKey)
        } else if (method.isAnnotationPresent(AdminSessionUser::class.java)) {
            authService.checkAdminAuth(authKey)
        }
    }

    @Around("execution(* kr.co.korbit.gia.controller.internal.*.*(..)) || execution(* kr.co.korbit.gia.controller.public.*.*(..)) || execution(* kr.co.korbit.gia.controller.admin.*.*(..))")
    @Throws(Throwable::class)
    fun around(call: ProceedingJoinPoint): Any? {
        var res: Any? = null
        val sig = call.signature as MethodSignature
        val method = sig.method
        val args = call.args
        val requestId: String = MDC.get("requestId")
        val requestUri: String = MDC.get("requestUri")
        val req: HttpServletRequest =
            (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

        try {
            logger.debug("around AOP in : $args")
            checkSession(call, req, method)
            res = call.proceed(args)
            when( res ) {
                is KorbitError -> return Response(false, res, MDC.get("requestId"), MDC.get("requestUri"), method.name.toUpperCase())
                is Response -> return res
                else -> return Response(true, res, MDC.get("requestId"), MDC.get("requestUri"), method.name.toUpperCase())
            }
        } catch(ex: SessionNotFoundException) {
            logger.error("proceed error return", ex.stackTraceString)
            val err = KorbitError.error(ErrorCode.E00003)
            return Response(false, err as Any, MDC.get("requestId"), MDC.get("requestUri"), method.name.toUpperCase())
        } catch (ex: Exception) {
            logger.error("proceed error return", ex.stackTraceString)
            val err = KorbitError.error(ErrorCode.E00000)
            err.description = ex.localizedMessage
            return Response(false, err as Any, MDC.get("requestId"), MDC.get("requestUri"), method.name.toUpperCase())

        }
    }
}