package kr.co.korbit.gia.jpa.test.service

import kr.co.korbit.common.extensions.stackTraceString
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.exception.SessionNotFoundException
import kr.co.korbit.gia.jpa.test.model.Session
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger(AuthService::class.java.name)

@Service
class AuthService {

    @Throws(SessionNotFoundException::class)
    fun checkAuth(authKey: String): Session {
        try {
            val session = Env.getTestSession()
            return session
        } catch(e: Throwable) {
            logger.error(e.stackTraceString)
            throw SessionNotFoundException(e)
        }
    }

    @Throws(SessionNotFoundException::class)
    fun checkGuestAuth(authKey: String): Session {
        try {
            val session = Env.getTestSession()
            return session
        } catch(e: Throwable) {
            logger.error(e.stackTraceString)
            throw SessionNotFoundException(e)
        }
    }

    @Throws(SessionNotFoundException::class)
    fun checkAdminAuth(authKey:String): Session {
        try {
            val session = Env.getTestSession()
            return session
        } catch(e: Throwable) {
            logger.error(e.stackTraceString)
            throw SessionNotFoundException(e)
        }
    }

    @Throws(SessionNotFoundException::class)
    fun checkAgentAuth(authKey:String): Session {
        try {
            val session = Env.getTestSession()
            return session
        } catch(e: Throwable) {
            logger.error(e.stackTraceString)
            throw SessionNotFoundException(e)
        }
    }
}