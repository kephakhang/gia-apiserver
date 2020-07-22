package kr.co.korbit.gia.config


import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.Date

class JwtAuthenticationFilter(authenticationManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {
    val authManager: AuthenticationManager

    init {
        setFilterProcessesUrl(JwtSecurityConstants.AUTH_LOGIN_URL)
        authManager = authenticationManager
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest,
                                       response: HttpServletResponse): Authentication {
        val authenticationToken = UsernamePasswordAuthenticationToken(request.getParameter("username"), request.getParameter("password"))
        return authManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse,
                                          filterChain: FilterChain, authentication: Authentication) {

        val user = authentication.getPrincipal() as User

        val roles = user.getAuthorities().map{it -> it.authority}

        val signingKey = JwtSecurityConstants.JWT_SECRET.toByteArray()

        val token = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .setHeaderParam("type", JwtSecurityConstants.TOKEN_TYPE)
            .setIssuer(JwtSecurityConstants.TOKEN_ISSUER)
            .setAudience(JwtSecurityConstants.TOKEN_AUDIENCE)
            .setSubject(user.getUsername())
            .setExpiration(Date(System.currentTimeMillis() + JwtSecurityConstants.EXPIRATION_TIME))
            .claim("role", roles)
            .compact()

        response.addHeader(JwtSecurityConstants.TOKEN_HEADER, JwtSecurityConstants.TOKEN_PREFIX + token)
    }
}
