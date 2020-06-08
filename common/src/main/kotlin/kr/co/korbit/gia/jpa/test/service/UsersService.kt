package kr.co.korbit.gia.jpa.test.service

import kr.co.korbit.gia.jpa.test.model.Users
import kr.co.korbit.gia.jpa.test.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsersService {
    @Autowired
    val usersRepository: UserRepository? = null

    fun getUser(id: Long): Users? {
//        val user: Users? = usersRepository?.findById(id)?.get()
//        return user

        usersRepository?.let {
            return it.findById(id).get()
        }
        return null
    }
}