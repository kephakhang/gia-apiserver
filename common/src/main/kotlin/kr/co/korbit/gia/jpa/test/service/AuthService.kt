package kr.co.korbit.gia.jpa.test.service

import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.exception.SessionNotFoundException
import kr.co.korbit.gia.jpa.test.model.Session
import org.springframework.stereotype.Service

@Service
class AuthService {

    @Throws(SessionNotFoundException::class)
    fun checkAuth(authKey: String): Session {
        return Env.getTestSession()
    }

    @Throws(SessionNotFoundException::class)
    fun checkGuestAuth(authKey: String): Session {
        return Env.getTestSession()
    }

    @Throws(SessionNotFoundException::class)
    fun checkAdminAuth(authKey:String): Session {
        return Env.getTestSession()
    }

    @Throws(SessionNotFoundException::class)
    fun checkAgentAuth(authKey:String): Session {
        return Env.getTestSession()
    }
}