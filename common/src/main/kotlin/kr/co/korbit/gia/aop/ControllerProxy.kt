package kr.co.korbit.gia.aop

import kr.co.korbit.common.error.KorbitError
import kr.co.korbit.common.extensions.stackTraceString
import kr.co.korbit.gia.util.KeyGenerator
import kr.co.korbit.gia.annotation.AdminSessionUser
import kr.co.korbit.gia.annotation.AgentSessionUser
import kr.co.korbit.gia.annotation.SkipSessionCheck
import kr.co.korbit.gia.exception.SessionNotFoundException
import kr.co.korbit.gia.jpa.common.Response
import kr.co.korbit.gia.jpa.test.model.Admin
import kr.co.korbit.gia.jpa.test.model.Agent
import kr.co.korbit.gia.jpa.test.model.Session
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
import java.lang.reflect.ParameterizedType
import org.slf4j.MDC
import javax.servlet.http.HttpServletRequest


private val logger = KotlinLogging.logger(ControllerProxy::class.java.name)

@Aspect
@Configuration
class ControllerProxy {


    @Autowired
    var context: ApplicationContext? = null

    @Around("execution(* kr.co.korbit.gia.controller.*.*(..))")
    @Throws(
        Throwable::class
    )
    fun around(call: ProceedingJoinPoint): Any? {
        var res: Any? = null
        val sig =
            call.signature as MethodSignature
        val method = sig.method
        val args = call.args
        logger.debug("around AOP in : $args")
        val requestId: String = KeyGenerator.generateOrderNo()
        MDC.put("requestId", "--:" + KeyGenerator.generateOrderNo().toString() + ":--")
        val req: HttpServletRequest =
            (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        MDC.put("requestUri", req.requestURI)

        /*
        //MGK_ADD : OAuth2 인증 체크 부분 추가 [
        if (StoreApplication.oauth2Enable) {
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

        var sessionMemberId: Long? = null
        if (!method.isAnnotationPresent(SkipSessionCheck::class.java) &&
            !method.isAnnotationPresent(AgentSessionUser::class.java) &&
            !method.isAnnotationPresent(AdminSessionUser::class.java)
        ) {
            val genericParameterTypes = method.genericParameterTypes
            val returnType = method.returnType
            var i = 0
            var existSession = false
            for (genericParameterType in genericParameterTypes) {
                if (genericParameterType is ParameterizedType) {
                    continue
                } else {
                    val cls = genericParameterType as Class<*>
                    if (Session::class.java.isAssignableFrom(cls)) {
                        val session: Session = args[i] as Session
                        if (session == null) {

                            val sne = SessionNotFoundException()
                            return sne
                        }
                    }
                }
                i++
            }

//			if( !existSession && StoreApplication.SERVER_NAME.equals("LOCAL") ) {
//				existSession = true ;
//			}
            if (!existSession) {
                val sne = SessionNotFoundException()
                return sne
            }
        } else if (method.isAnnotationPresent(AgentSessionUser::class.java)) {
            val genericParameterTypes = method.genericParameterTypes
            val returnType = method.returnType
            val i = 0
            var existAgentAccessToken = false
            for (genericParameterType in genericParameterTypes) {
                if (genericParameterType is ParameterizedType) {
                    continue
                } else {
                    val cls = genericParameterType as Class<*>
                    if (Agent::class.java.isAssignableFrom(cls)) {
                        val agent: Agent = args[i] as Agent
                        if (agent != null) {
                            existAgentAccessToken = true
                            sessionMemberId = agent.id
                            break
                        }
                    }
                }
            }
            if (!existAgentAccessToken) {
                val sne = SessionNotFoundException()
                //ToDo Return ???
                return sne
            }
        } else if (method.isAnnotationPresent(AdminSessionUser::class.java)) {
            val genericParameterTypes = method.genericParameterTypes
            val returnType = method.returnType
            val i = 0
            var existAdminSessionUser = false
            for (genericParameterType in genericParameterTypes) {
                if (genericParameterType is ParameterizedType) {
                    continue
                } else {
                    val cls = genericParameterType as Class<*>
                    if (Session::class.java.isAssignableFrom(cls)) {
                        val admin: Admin = args[i] as Admin
                        if (admin != null) {
                            existAdminSessionUser = true
                            sessionMemberId = admin.id
                            break
                        }
                    }
                }
            }
            if (!existAdminSessionUser) {
                val sne =
                    SessionNotFoundException()

                //ToDo return ???
                return sne
            }
        }
        try {
            res = call.proceed(args)
            when( res ) {
                is KorbitError -> return Response(false, MDC.get("requestId"), MDC.get("requestUri"), res)
                else -> return Response(true, MDC.get("requestId"), MDC.get("requestUri"), res)
            }
        } catch (ex: Exception) {
            logger.error("proceed error return", ex.stackTraceString)

            //ToDo exception ???
            return ex
        }
    }
}