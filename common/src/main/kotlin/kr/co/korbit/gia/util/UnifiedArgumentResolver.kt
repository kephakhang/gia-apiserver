package kr.co.korbit.gia.util

import com.google.api.gax.rpc.UnknownException
import kr.co.korbit.exception.stackTraceString
import kr.co.korbit.gia.annotation.AdminSessionUser
import kr.co.korbit.gia.annotation.AgentSessionUser
import kr.co.korbit.gia.annotation.GuestSessionUser
import kr.co.korbit.gia.annotation.SkipSessionCheck
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.exception.SessionNotFoundException
import kr.co.korbit.gia.jpa.test.model.Session
import kr.co.korbit.gia.jpa.test.service.AuthService
import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest



class UnifiedArgumentResolver : HandlerMethodArgumentResolver, ApplicationContextAware {

    val logger = KotlinLogging.logger(UnifiedArgumentResolver::class.java.name)

    @Autowired
    var context: ApplicationContext? = null

    @Throws(Exception::class)
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory
    ): Any? {
        var authKey = (webRequest.nativeRequest as HttpServletRequest).getHeader("Authorization")
        if (authKey == null || authKey.isEmpty()) {
            val cookies =
                (webRequest.nativeRequest as HttpServletRequest).cookies
            if (cookies != null) {
                for (cookie in cookies) {
                    if (cookie.name.equals("Korbit-Authorization", ignoreCase = true)) {
                        authKey = cookie.value
                        break
                    }
                }
            }
            if (authKey == null || authKey.isEmpty()) authKey =
                (webRequest.nativeRequest as HttpServletRequest).getParameter("Authorization")
        }
        val method = parameter.method
        val possibleSkip = method.isAnnotationPresent(SkipSessionCheck::class.java)
        if (possibleSkip && StringUtils.isEmpty(authKey)) return null

        // 외부 업체 Agent API 연동시...
        val isAgentUser = method.isAnnotationPresent(AgentSessionUser::class.java)
        // 비회원 인증
        val isGuestUser = method.isAnnotationPresent(GuestSessionUser::class.java)
        // 관리자 인증
        val isAdminUser = method.isAnnotationPresent(AdminSessionUser::class.java)
        if (isAgentUser) {
            return try {
                val authSvc: AuthService = context!!.getBean(AuthService::class.java)
                val agentId =
                    (webRequest.nativeRequest as HttpServletRequest).getHeader("User-Agent")
                val timestamp =
                    (webRequest.nativeRequest as HttpServletRequest).getHeader("timestamp")
                logger.info("Agent sessionKey : $authKey")
                logger.info("Agent id : $agentId")
                logger.info("Agent timestamp : $timestamp")
                val headerNames =
                    (webRequest.nativeRequest as HttpServletRequest).headerNames
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        val header = headerNames.nextElement()
                        val value =
                            (webRequest.nativeRequest as HttpServletRequest).getHeader(header)
                        println("Agent header:value - $header:$value")
                        logger.info("Agent header:value - $header:$value")
                    }
                }
                authSvc.checkAgentAuth(agentId, timestamp, authKey)
            } catch (ex: SessionNotFoundException) {
                logger.error{ ex.stackTraceString }
                null
            } catch (ex: Exception) {
                throw ex
            }
        } else if (isGuestUser) {
            val authSvc: AuthService = context!!.getBean(AuthService::class.java)
            logger.info("Guest Authorization : $authKey")
            return authSvc.checkGuestAuth(authKey)
        } else if (isAdminUser) {
            val authSvc: AuthService = context!!.getBean(AuthService::class.java)
            logger.info("Admin Authorization : $authKey")
            return authSvc.checkAdminAuth(authKey)
        } else {
            try {

                // 로칼 테스트 용
                if (Env.branch === "local") {
                    return Env.getTestSession()
                }
                logger.info("context : " + context.toString())
                val authSvc: AuthService = context!!.getBean(AuthService::class.java)
                logger.info("authSvc : $authSvc")
                logger.info("sessionKey : $authKey")
                val session: Session = authSvc.checkAdminAuth(authKey)
                logger.debug("Session data : $session")
                return session
            } catch (ex: SessionNotFoundException) {
                if (!possibleSkip) {
                    logger.error { ex.stackTraceString }
                    return null
                }
            } catch (ex: Exception) {
                throw Exception("UnknownException : UnifiedArgumentResolver Error : " + ex.message)
            }
        }
        return null
    }

    override fun supportsParameter(arg: MethodParameter): Boolean {
        return if (Session::class.java.isAssignableFrom(arg.parameterType)) true else false
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }
}