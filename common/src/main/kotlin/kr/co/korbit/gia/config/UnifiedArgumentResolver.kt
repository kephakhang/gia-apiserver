package kr.co.korbit.gia.config

import kr.co.korbit.common.extensions.stackTraceString
import kr.co.korbit.gia.annotation.AdminSessionUser
import kr.co.korbit.gia.annotation.AgentSessionUser
import kr.co.korbit.gia.annotation.SkipSessionCheck
import kr.co.korbit.gia.exception.SessionNotFoundException
import kr.co.korbit.gia.jpa.test.model.Session
import kr.co.korbit.gia.jpa.test.service.AuthService
import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Service
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.lang.reflect.Method
import javax.servlet.http.HttpServletRequest

private val logger = KotlinLogging.logger(UnifiedArgumentResolver::class.java.name)

@Service
class UnifiedArgumentResolver : HandlerMethodArgumentResolver, ApplicationContextAware {

    @Autowired
    var context: ApplicationContext? = null

    @Throws(SessionNotFoundException::class)
    fun checkSession(req: HttpServletRequest, method: Method): Session? {
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
        MDC.put("authKey", authKey ?: "NULL")
        try {
            if (method.isAnnotationPresent(SkipSessionCheck::class.java)) {
                return null
            } else if (method.isAnnotationPresent(AgentSessionUser::class.java)) {
                return authService.checkAgentAuth(authKey)
            } else if (method.isAnnotationPresent(AdminSessionUser::class.java)) {
                return authService.checkAdminAuth(authKey)
            } else { // public session
                //ToDo remove test authKey
                authKey = "TEST"
                return authService.checkAuth(authKey)
            }
        } catch(ex: Exception) {
            return null
        }
    }

    override fun supportsParameter(methodParameter: MethodParameter): Boolean {
        return Session::class.java.isAssignableFrom(methodParameter.getParameterType())
    }

    @Throws(Exception::class)
    override fun resolveArgument(
        methodParameter: MethodParameter,
        modelAndViewContainer: ModelAndViewContainer,
        nativeWebRequest: NativeWebRequest,
        webDataBinderFactory: WebDataBinderFactory
    ): Any? {

        return try {
            if (methodParameter.method!!.isAnnotationPresent(SkipSessionCheck::class.java)) {
                null
            } else {
                val session: Session? =
                    checkSession(nativeWebRequest.nativeRequest as HttpServletRequest, methodParameter.method)
                session as Any
            }
        } catch(e: Throwable) {
            logger.error(e.stackTraceString)
            null
        }
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.context = applicationContext
    }
}