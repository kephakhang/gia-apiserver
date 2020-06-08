package kr.co.korbit.gia.util

import kr.co.korbit.common.extensions.traceString
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoggingFilter : Filter {
    private val id = AtomicLong(1)

    @Throws(ServletException::class)
    override fun init(arg0: FilterConfig) {
    }

    override fun destroy() {}

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        req: ServletRequest, res: ServletResponse,
        chain: FilterChain
    ) {

        val requestId = MDC.get("requestId")
        val requestUri = MDC.get("requestUri")
        val startTime = System.nanoTime()
//        val requestId = id.incrementAndGet()
//        val startTime = System.nanoTime()
        var isException = false
        try {

            if ( "/(health|.+\\.(ico|js|css|html|jpg|jpeg|png|gif|svg))".toRegex().containsMatchIn(requestUri) ) {
                req!!.setAttribute("ignoreLogging", true)
            }

            //logger.debug( "###request### : " + req.getRequestURI() + req.getAttribute("systemBaseUrl")) ;
            val msg = StringBuilder()
            msg.append(CRLF).append("################################")
                .append(CRLF).append(REQUEST_PREFIX)
                .append(requestUri).append(":").append(requestId)
            logger.debug(msg.toString())
            chain.doFilter(req, res)
        } catch (ex: ServletException) {
            isException = true
            ex.printStackTrace()
            throw ex
        } catch (ex: IOException) {
            isException = true
            ex.printStackTrace()
            throw ex
        } catch (ex: Exception) {
            isException = true
            ex.printStackTrace()
            throw ex
        } finally {
            if (logger.isDebugEnabled) {
                logging(startTime, requestId, requestUri, isException, req, res)
            }
        }
    }

    private fun isHtml(request: HttpServletRequest?): Boolean {
        return try {
            val uri = request!!.requestURI.toLowerCase()
            if (uri.indexOf(".ftl") > 0 || uri.indexOf(".html") > 0 || uri.indexOf(".js") > 0 || uri.indexOf(".htm") > 0 || uri.indexOf(
                    "/jsp"
                ) > 0 || uri.indexOf(".css") > 0
            ) true else false
        } catch (ex: Exception) {
            false
        }
    }

    private fun isMultipart(request: HttpServletRequest?): Boolean {
        return (request!!.contentType != null
                && request.contentType.startsWith("multipart/form-data"))
    }

    private fun isFormSubmit(request: HttpServletRequest?): Boolean {
        return (request!!.contentType != null
                && request.contentType.startsWith(
            "application/x-www-form-urlencoded"
        ))
    }

    private fun getAuthKey(request: HttpServletRequest?): String? {
        var authKey = request!!.getHeader("Authorization")
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
        if (!isHtml(request)) incRes = true
        if (isMultipart(request)) {
            val tm = StringBuilder()
            val en: Enumeration<*> = request!!.headerNames
            tm.append(CRLF).append("Request Header").append(CRLF)
            while (en.hasMoreElements()) {
                val hn = en.nextElement() as String
                val hv = request.getHeader(hn)
                if (hn != null && hv != null) tm.append(hn).append(": ").append(hv).append(CRLF)
            }
            logger.debug(tm.toString())
        }
        if (logging) {
            val msg = StringBuilder()
            val reqUri = request!!.requestURI
            if (reqUri.contains("Payment") || reqUri.contains("INIpay") || reqUri.contains("payment")) incRes = true
            if (!isException) {

				msg.append(CRLF).append("################################")
						.append(CRLF).append(REQUEST_PREFIX)
					.append(CRLF).append(REQUEST_PREFIX)
					.append(reqUri).append(":").append(requestId);

            } else {
                msg.append(CRLF).append("###Exception####################")
                    .append(CRLF).append(REQUEST_PREFIX)
                    .append(reqUri).append(":").append(requestId)
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
            if (authKey!!.isNotEmpty()) {
                msg.append(CRLF).append("SessionKey: ").append(authKey)
            }


            /*msg.append(CRLF).append("Remote-IP: ")
					.append(ThcUtil.getClientIP(request));*/msg.append(CRLF)
                .append("Content-Type: ")
                .append(request.contentType)
            if (!isMultipart(request)) {
                msg.append(CRLF).append("Request Body: ").append(reqUri).append(":")
                    .append(requestId).append(CRLF)
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
                    msg.append(request.traceString().toByte())
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
                msg.append(CRLF).append(response.traceString().toByte())
            }
            msg.append(CRLF).append("################################").append(requestId)
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

    companion object {
        protected val logger = LoggerFactory.getLogger(LoggingFilter::class.java)
        private const val CRLF = "\n"
        private const val REQUEST_PREFIX = "Request: "
        private const val RESPONSE_PREFIX = "Response: "
    }
}