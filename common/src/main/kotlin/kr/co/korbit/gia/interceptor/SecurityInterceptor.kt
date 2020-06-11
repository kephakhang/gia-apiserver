package kr.co.korbit.gia.interceptor

import mu.KotlinLogging
import org.slf4j.LoggerFactory
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
        val uri = request.requestURI
        //val session = request.session
        return try {
            logger.debug("method : " + request.method)
            logger.debug("uri : $uri")
            if (request.method.toUpperCase() == "OPTIONS") {
                if (uri.startsWith("/gia/") || uri.startsWith("/api/")) {
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
                    response.writer.write("{\"success\":false}")
                    response.writer.flush()
                    false
                }
            } else {
                logger.debug("URI - {}", uri)
                if (uri.startsWith("/gia/") || uri.startsWith("/api/")) {
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
                    response.writer.write("{\"success\":false}")
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
            response.writer.write(e.message)
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