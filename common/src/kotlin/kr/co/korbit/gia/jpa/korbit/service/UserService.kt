package kr.co.korbit.gia.jpa.test.service

import kr.co.korbit.gia.jpa.test.model.User
import kr.co.korbit.gia.jpa.test.respository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService {
    @Autowired
    val userRepository: UserRepository? = null

    fun getUser(id: Long): User? {
        val user: User? = userRepository?.findById(id)?.get()
        return user
    }
}