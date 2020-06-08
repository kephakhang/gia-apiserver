package kr.co.korbit.gia.jpa.test.repository

import kr.co.korbit.gia.jpa.test.model.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional(transactionManager = "jpaTestTransactionManager")
interface UserRepository : JpaRepository<Users, Long> {
    override fun findById(id: Long): Optional<Users>
}
