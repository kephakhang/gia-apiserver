package kr.co.korbit.gia.jpa.korbit.repository

import kr.co.korbit.gia.jpa.korbit.model.LneQuizz
import kr.co.korbit.gia.jpa.test.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional(transactionManager = "jpaTestTransactionManager")
interface LneQuizzRepository : JpaRepository<LneQuizz, Long> {
    override fun findById(id: Long): Optional<LneQuizz>
}
