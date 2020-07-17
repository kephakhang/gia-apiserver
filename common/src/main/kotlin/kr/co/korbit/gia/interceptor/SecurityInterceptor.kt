package kr.co.korbit.gia.interceptor

import kr.co.korbit.common.error.ErrorCode
import kr.co.korbit.common.error.KorbitError
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.common.Response
import mu.KotlinLogging
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SecurityInterceptor : HandlerInterceptorAdapter() {

    private val logger = KotlinLogging.logger(SecurityInterceptor::class.java.name)
    val PAGE_URL = "pageUrl"

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logger.debug("[preHandle] - {} {}", request.method, request.requestURI)
        val uri: String = request.requestURI
        //val session = request.session
        return try {
            logger.debug("method : " + request.method)
            logger.debug("uri : $uri")
            if (request.method.toUpperCase() == "OPTIONS") {
                if (uri.startsWith("/internal/") || uri.startsWith("/public/") || uri.startsWith("/admin/")) {
                    response.reset()
                    response.setHeader("Access-Control-Allow-Origin", "*")
                    response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT")
                    response.setHeader("Access-Control-Max-Age", "3600")
                    response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, sessionKey, Cache-Control, Content-Type, Accept, Authorization")
                    response.contentType = "application/json; charset=utf-8"
                    response.characterEncoding = "utf-8"
                    response.status = HttpServletResponse.SC_OK
                    response.writer.write("{\"success\":true}")
                    response.writer.flush()
                    true
                } else {
                    response.reset()
                    response.setHeader("Access-Control-Allow-Origin", "*")
                    response.setHeader("Access-Control-Allow-Methods", "")
                    response.setHeader("Access-Control-Max-Age", "10")
                    response.contentType = "application/json; charset=utf-8"
                    response.characterEncoding = "utf-8"
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    val res = Response(false, KorbitError.error(ErrorCode.E00002), "0",  uri, request.method.toUpperCase())
                    response.writer.write(Env.objectMapper.writeValueAsString(res))
                    response.writer.flush()
                    false
                }
            } else {
                logger.debug("URI - {}", uri)
                if (uri.endsWith(".html") || uri.endsWith(".css") || uri.endsWith(".js") ||
                    uri.endsWith(".jpg") || uri.endsWith(".gif") || uri.endsWith(".png") ||
                    uri.endsWith(".ico") || uri.endsWith(".svg") || uri.equals("/error") ||
                    uri.startsWith("/configuration/ui") || uri.startsWith("/configuration/security") ||
                    uri.contains("\\/swagger".toRegex()) || uri.contains("\\/webjars\\/".toRegex())  || uri.equals("/v2/api-docs") ||
                    uri.startsWith("/internal/") || uri.startsWith("/public/") || uri.startsWith("/admin/")) {
                    response.setHeader("Access-Control-Allow-Origin", "*")
                    response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT")
                    response.setHeader("Access-Control-Max-Age", "3600")
                    response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, sessionKey, Cache-Control, Content-Type, Accept, Authorization")
                    true
                } else {
                    response.reset()
                    response.setHeader("Access-Control-Allow-Origin", "*")
                    response.setHeader("Access-Control-Allow-Methods", "")
                    response.setHeader("Access-Control-Max-Age", "10")
                    response.contentType = "application/json; charset=utf-8"
                    response.characterEncoding = "utf-8"
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    val res = Response(false, KorbitError.error(ErrorCode.E00002), "0",  uri, request.method.toUpperCase())
                    response.writer.write(Env.objectMapper.writeValueAsString(res))
                    response.writer.flush()
                    false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            response.reset()
            response.setHeader("Access-Control-Allow-Origin", "*")
            response.setHeader("Access-Control-Allow-Methods", "")
            response.setHeader("Access-Control-Max-Age", "10")
            response.setHeader("Access-Control-Allow-Headers", "x-requested-with")
            response.contentType = "application/json; charset=utf-8"
            response.characterEncoding = "utf-8"
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            val res = Response(false, KorbitError.error(ErrorCode.E00002), "0",  uri, request.method.toUpperCase())
            response.writer.write(Env.objectMapper.writeValueAsString(res))
            response.writer.flush()
            false
        }
    }

    @Throws(Exception::class)
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any,
                            modelAndView: ModelAndView?) {
    }

    @Throws(Exception::class)
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
    }

}