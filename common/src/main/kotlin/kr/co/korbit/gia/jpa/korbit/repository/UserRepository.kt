package kr.co.korbit.gia.jpa.korbit.repository

import kr.co.korbit.gia.jpa.korbit.model.User
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomUserRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional(transactionManager = "jpaKorbitTransactionManager")
interface UserRepository : JpaRepository<User, Long>, CustomUserRepository {
    override fun findById(id: Long): Optional<User>
    fun findByFailedAttemptsAfter(attempt: Int): List<User>
    fun findByEmail(email:String): User?
}
