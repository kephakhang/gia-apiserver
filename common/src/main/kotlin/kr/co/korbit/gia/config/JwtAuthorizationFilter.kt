package kr.co.korbit.gia.config

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import kr.co.korbit.gia.aop.ControllerProxy
import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException

private val logger = KotlinLogging.logger(JwtAuthorizationFilter::class.java.name)

@Suppress("UNCHECKED_CAST")
class JwtAuthorizationFilter(authenticationManager: AuthenticationManager) : BasicAuthenticationFilter(authenticationManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse,
                                  filterChain: FilterChain) {
        val authentication = getAuthentication(request)
        val header = request.getHeader(JwtSecurityConstants.TOKEN_HEADER)

        if (StringUtils.isEmpty(header) || !header.startsWith(JwtSecurityConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response)
            return
        }

        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader(JwtSecurityConstants.TOKEN_HEADER)
        if (StringUtils.isNotEmpty(token)) {
            try {
                val signingKey = JwtSecurityConstants.JWT_SECRET.toByteArray()

                val parsedToken = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token.replace("Bearer ", ""))

                val username = parsedToken
                    .getBody()
                    .getSubject()

                val authorities =  (parsedToken.getBody()["role"] as ArrayList<String>).map { SimpleGrantedAuthority(it) }

                if (StringUtils.isNotEmpty(username)) {
                    return UsernamePasswordAuthenticationToken(username, null, authorities)
                }
            } catch (exception: ExpiredJwtException) {
                logger.warn("Request to parse expired JWT : $token failed : ${exception.message}")
            } catch (exception: UnsupportedJwtException) {
                logger.warn("Request to parse unsupported JWT : $token failed : ${exception.message}")
            } catch (exception: MalformedJwtException) {
                logger.warn("Request to parse invalid JWT : $token failed : ${exception.message}")
            } catch (exception: SignatureException) {
                logger.warn("Request to parse JWT with invalid signature : $token failed : ${exception.message}")
            } catch (exception: IllegalArgumentException) {
                logger.warn("Request to parse empty or null JWT : $token failed : ${exception.message}")
            }
        }

        return null
    }
}
