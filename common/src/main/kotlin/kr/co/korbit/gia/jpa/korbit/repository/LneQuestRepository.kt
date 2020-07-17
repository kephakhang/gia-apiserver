package kr.co.korbit.gia.jpa.korbit.repository

import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuestRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.*
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import javax.persistence.QueryHint

@Repository
@Transactional(transactionManager = "jpaKorbitTransactionManager")
interface LneQuestRepository: JpaRepository<LneQuest, Long>, CustomLneQuestRepository {

    override fun findById(id: Long): Optional<LneQuest>
    override fun findAll(pageable: Pageable): Page<LneQuest>

//    @EntityGraph(attributePaths = ["quizList"])
    fun findAllByTypeAndCreatedAtBetween(@Param("type")type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest>

    // JPQL by annotation @Query : 테이블 명은 Entity Name 으로 인식한다.
    //@EntityGraph("LneQuest.all") - Join Fetch
    //@Lock       select ** from ...  for UPDATE - select 중에  update 못하게 Lock 을 건다.
    //@Lock(LockModeType.PESSIMISTIC_READ) - DB Lock (꼭 필요한 경우 아니면 사용 안하는게 좋다. 회계관리 등 대사가 중요한 경우 사용 가능)
    // 스냅샷 업이 ReadOnly 로만 수행 쿼리후 수정된 내용 DB 에 반영안됨
    @QueryHints(value = [QueryHint(name = "org.hibernate.readOnly", value = "true")])
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
