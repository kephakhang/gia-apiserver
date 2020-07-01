package kr.co.korbit.gia.config

import com.sun.org.apache.xml.internal.security.algorithms.Algorithm
import kr.co.korbit.gia.jpa.test.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.Elements.JWT
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthorizationFilter(
    authenticationManager: AuthenticationManager?,
    private val userRepository: UserRepository
) :
    BasicAuthenticationFilter(authenticationManager) {

    // endpoint every request hit with authorization
    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse?,
        chain: FilterChain
    ) {
        // Read the Authorization header, where the JWT Token should be
        val header = request.getHeader(JwtProperties.HEADER_STRING)

        // If header does not contain BEARER or is null delegate to Spring impl and exit
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            // rest of the spring pipeline
            chain.doFilter(request, response)
            return
        }

        // If header is present, try grab user principal from database and perform authorization
        val authentication: Authentication? = getUsernamePasswordAuthentication(request)
        SecurityContextHolder.getContext().setAuthentication(authentication)

        // Continue filter execution
        chain.doFilter(request, response)
    }

    private fun getUsernamePasswordAuthentication(request: HttpServletRequest): Authentication? {
//        val token = request.getHeader(JwtProperties.HEADER_STRING)
//        if (token != null) {
//            // parse the token and validate it (decode)
//            val username: String = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.toByteArray()))
//                .build()
//                .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""))
//                .getSubject()
//
//            // Search in the DB if we find the user by token subject (username)
//            // If so, then grab user details and create spring auth token using username, pass, authorities/roles
//
//            if (username != null) {
//                val user: User = userRepository.findByUsername(username)
//                val principal = UserPrincipal(user)
//                return UsernamePasswordAuthenticationToken(username, null, principal.getAuthorities())
//            }
//            return null
//        }
        return null
    }

}