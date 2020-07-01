package kr.co.korbit.gia.service.test


import kr.co.korbit.common.extensions.stackTraceString
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.exception.SessionNotFoundException
import kr.co.korbit.gia.jpa.korbit.model.dto.Session
import mu.KotlinLogging
import org.springframework.stereotype.Service


private val logger = KotlinLogging.logger(AuthService::class.java.name)

@Service
class AuthService {

//    fun getUsernamePasswordAuthentication(token: String): Authentication? {
//
//            // parse the token and validate it (decode)
//            val username: String = Elements.JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.toByteArray()))
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
//
//        return null
//    }

    @Throws(SessionNotFoundException::class)
    fun checkAuth(authKey: String): Session {
        try {
//            // If header does not contain BEARER or is null delegate to Spring impl and exit
//
//
//                val authentication: Authentication = getUsernamePasswordAuthentication(request)
//                SecurityContextHolder.getContext().authentication = authentication
//            }
//
//
//
//            // If header is present, try grab user principal from database and perform authorization
//
//            // If header is present, try grab user principal from database and perform authorization
//            val authentication: Authentication = getUsernamePasswordAuthentication(request)
//            SecurityContextHolder.getContext().authentication = authentication

            val session = Session(Env.getTestUser())
            return session
        } catch(e: Throwable) {
            logger.error(e.stackTraceString)
            throw SessionNotFoundException(e)
        }
    }

    @Throws(SessionNotFoundException::class)
    fun checkGuestAuth(authKey: String): Session {
        try {
            val session = Session(Env.getTestUser())
            return session
        } catch(e: Throwable) {
            logger.error(e.stackTraceString)
            throw SessionNotFoundException(e)
        }
    }

    @Throws(SessionNotFoundException::class)
    fun checkAdminAuth(authKey:String): Session {
        try {
            val session = Session(Env.getTestUser())
            return session
        } catch(e: Throwable) {
            logger.error(e.stackTraceString)
            throw SessionNotFoundException(e)
        }
    }

    @Throws(SessionNotFoundException::class)
    fun checkAgentAuth(authKey:String): Session {
        try {
            val session = Session(Env.getTestUser())
            return session
        } catch(e: Throwable) {
            logger.error(e.stackTraceString)
            throw SessionNotFoundException(e)
        }
    }
}