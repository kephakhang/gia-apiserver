package kr.co.korbit.gia.service.test

import kr.co.korbit.gia.jpa.test.model.User
import kr.co.korbit.gia.jpa.test.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    val usersRepository: UserRepository? = null

    fun getUser(id: Long): User? {
        usersRepository?.let {
            val user:User? = it.findById(id).get()
            return user
        }
        return null
    }
}