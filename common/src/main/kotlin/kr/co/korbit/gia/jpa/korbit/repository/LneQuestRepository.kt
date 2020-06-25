package kr.co.korbit.gia.jpa.korbit.repository

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.common.WhereBuilder
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuizz
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuestRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Repository
@Transactional(transactionManager = "jpaKorbitTransactionManager")
interface LneQuestRepository : JpaRepository<LneQuest, Long>, CustomLneQuestRepository {

    override fun findById(id: Long): Optional<LneQuest>
    override fun findAll(pageable: Pageable): Page<LneQuest>
    fun findAllByTypeAndCreatedAtBetween(type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest>
}
