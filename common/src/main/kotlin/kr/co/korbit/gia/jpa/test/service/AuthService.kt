package kr.co.korbit.gia.jpa.test.service

import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.test.model.Session
import org.springframework.stereotype.Service

@Service
class AuthService {

    fun checkAuth(authKey: String): Session {
        return Env.getTestSession()
    }

    fun checkGuestAuth(authKey: String): Session {
        return Env.getTestSession()
    }

    fun checkAdminAuth(authKey:String): Session {
        return Env.getTestSession()
    }

    fun checkAgentAuth(agentId: String, timestamp: String, authKey:String): Session {
        return Env.getTestSession()
    }
}