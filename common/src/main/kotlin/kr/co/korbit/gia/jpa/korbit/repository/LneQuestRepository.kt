package kr.co.korbit.gia.jpa.korbit.repository

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.common.WhereBuilder
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuiz
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.LockModeType

@Repository
@Transactional(transactionManager = "jpaKorbitTransactionManager")
interface LneQuestRepository: JpaRepository<LneQuest, Long>, CustomLneQuestRepository {

    override fun findById(id: Long): Optional<LneQuest>
    override fun findAll(pageable: Pageable): Page<LneQuest>

//    @EntityGraph(attributePaths = ["quizList"])
    fun findAllByTypeAndCreatedAtBetween(@Param("type")type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest>

    // JPQL by annotation @Query : 테이블 명은 Entity Name 으로 인식한다.
    //@EntityGraph("LneQuest.all")
    //@QueryHints
    //@Lock       select ** from ...  for UPDATE
    //@Lock(LockModeType.PESSIMISTIC_READ)
    @EntityGraph(attributePaths = ["quizList"])
    @Query(value = "select q " +
                " from LneQuest q where 1=1 " +
                " and (:type is null or q.type = :type) " +
                " and (:from is null or :from < q.createdAt) " +
                " and (:to is null or q.createdAt < :to) ",
            countQuery = "select count(q)" +
                " from LneQuest q where 1=1 " +
                " and (:type is null or q.type = :type) " +
                " and (:from is null or :from < q.createdAt) " +
                " and (:to is null or q.createdAt < :to)", nativeQuery = false)
    fun findAllByJpql(@Param("type")type: String?,
                      @Param("from")from: LocalDateTime?,
                      @Param("to")to: LocalDateTime?, pageable: Pageable): Page<LneQuest>


}
