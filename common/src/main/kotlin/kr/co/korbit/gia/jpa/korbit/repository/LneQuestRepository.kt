package kr.co.korbit.gia.jpa.korbit.repository

import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.test.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Repository
@Transactional(transactionManager = "jpaTestTransactionManager")
interface LneQuestRepository : JpaRepository<LneQuest, Long> {
    override fun findById(id: Long): Optional<LneQuest>
    override fun findAll(pageable: Pageable): Page<LneQuest>

    fun findAllByCreatedAtBetween(@Param("from")from: LocalDateTime, @Param("to")to: LocalDateTime, pageable: Pageable): Page<LneQuest>


}
