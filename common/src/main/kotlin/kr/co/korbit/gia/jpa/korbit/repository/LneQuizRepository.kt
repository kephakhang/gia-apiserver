package kr.co.korbit.gia.jpa.korbit.repository

import kr.co.korbit.gia.jpa.korbit.model.LneQuiz
import kr.co.korbit.gia.jpa.test.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional(transactionManager = "jpaKorbitTransactionManager")
interface LneQuizRepository : JpaRepository<LneQuiz, Long> {
    override fun findById(id: Long): Optional<LneQuiz>
}
