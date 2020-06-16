package kr.co.korbit.gia.util

import kr.co.korbit.common.extensions.stackTraceString
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoggingFilter : Filter {

    protected val logger = LoggerFactory.getLogger(LoggingFilter::class.java)
    val CRLF = "\n"
    val REQUEST_PREFIX = "Request: "
    val RESPONSE_PREFIX = "Response: "


    @Throws(ServletException::class)
    override fun init(arg0: FilterConfig) {
    }

    override fun destroy() {}

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        req: ServletRequest, res: ServletResponse,
        chain: FilterChain
    ) {

        val requestId: String = KeyGenerator.generateOrderNo()
        MDC.put("requestId", requestId)
        val requestUri = (req as HttpServletRequest).requestURI
        MDC.put("requestUri", req.requestURI)

        val startTime = System.nanoTime()
        var isException = false
        try {

            if ( "/(health|.+\\.(ico|js|css|html|jpg|jpeg|png|gif|svg))".toRegex().containsMatchIn(requestUri) ) {
                req.setAttribute("ignoreLogging", true)
            }
            chain.doFilter(req, res)
        } catch (ex: ServletException) {
            isException = true
            logger.error (ex.stackTraceString)
            throw ex
        } catch (ex: IOException) {
            isException = true
            logger.error(ex.stackTraceString)
            throw ex
        } catch (ex: Exception) {
            isException = true
            logger.error (ex.stackTraceString)
            throw ex
        } finally {
            if (logger.isDebugEnabled) {
                logging(startTime, requestId, requestUri, isException, req, res)
            }
        }
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

    private fun getAuthKey(request: HttpServletRequest): String? {
        var authKey = request.getHeader("Authorization")
        if (authKey == null || authKey.isEmpty()) {
            val cookies = request.cookies
            if (cookies != null) {
                for (cookie in cookies) {
                    if (cookie.name.equals("Korbit-Authorization", ignoreCase = true)) {
                        authKey = cookie.value
                        break
                    }
                }
            }
            if (authKey == null || authKey.isEmpty()) authKey = request.getParameter("Authorization")
        }
        return authKey
    }

    private fun logging(
        startTime: Long,
        requestId: String,
        requestUri: String,
        isException: Boolean,
        req: ServletRequest?,
        res: ServletResponse?
    ) {
        val request: HttpServletRequest = req as HttpServletRequest
        val response: HttpServletResponse = res as HttpServletResponse
        val logging = true
        var incRes = false
        if (!isHtml(requestUri)) incRes = true
        if (isMultipart(request)) {
            val tm = StringBuilder()
            val en: Enumeration<*> = request.headerNames
            tm.append(CRLF).append("Request Header").append(CRLF)
            while (en.hasMoreElements()) {
                val hn = en.nextElement() as String
                val hv = request.getHeader(hn)
                if (hv != null) tm.append(hn).append(": ").append(hv).append(CRLF)
            }
            logger.debug(tm.toString())
        }
        if (logging) {
            val msg = StringBuilder()
            val reqUri = request.requestURI
            if (reqUri.contains("Payment") || reqUri.contains("INIpay") || reqUri.contains("payment")) incRes = true
            if (!isException) {

				msg.append(CRLF).append("################################").append("--:" + requestId + ":--[")
						.append(CRLF).append(REQUEST_PREFIX)
					.append(CRLF).append(REQUEST_PREFIX)
					.append(reqUri).append(":")

            } else {
                msg.append(CRLF).append("###Exception####################").append("--:" + requestId + ":--[")
                    .append(CRLF).append(REQUEST_PREFIX)
                    .append(reqUri).append(":")
            }
            if (request.queryString != null
                && !request.queryString.isEmpty()
            ) msg.append('?').append(request.queryString)
            msg.append(" ").append(request.method)
            val session = request.getSession(false)
            if (session != null) {
                msg.append(CRLF).append("SESSION ID: ").append(session.id)
            }
            val authKey = getAuthKey(request)
            authKey ?.let {
                if (it.isNotEmpty()) {
                    msg.append(CRLF).append("authKey: ").append(authKey).append(CRLF)
                }
            }

            var ip: String? = req.getHeader("X-FORWARDED-FOR")
            if (ip == null) ip = req.getRemoteAddr()

            ip?.let {
                msg.append(CRLF).append("Remote-IP: ")
                    .append(ip).append(CRLF)
            }

            msg.append("Content-Type: ").append(request.contentType)
            if (!isMultipart(request)) {
                msg.append(CRLF).append("Request Body: ").append(reqUri).append(":").append(CRLF)
                if (isFormSubmit(request)) {
                    var firstParam = true
                    val e: Enumeration<*> = request.parameterNames
                    while (e
                            .hasMoreElements()
                    ) {
                        val pn = e.nextElement() as String
                        val pv = request.getParameterValues(pn)
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
                    msg.append(String(RequestWrapper(request).toByteArray()))
                }
            }
            msg.append(CRLF).append("Elapsed Time: ")
                .append(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime))
                .append(" msec")
            if (reqUri.indexOf("/attachment/image") >= 0 || reqUri.indexOf("/console/font") >= 0) {
                incRes = false
            }
            if (!isException && incRes) {
                msg.append(CRLF).append("-------------------------------")
                    .append(CRLF).append(RESPONSE_PREFIX)
                msg.append(CRLF).append(String(ResponseWrapper(response).toByteArray()))
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
    }
}