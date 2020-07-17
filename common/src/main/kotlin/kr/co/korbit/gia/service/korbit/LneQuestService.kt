package kr.co.korbit.gia.service.korbit

import kr.co.korbit.gia.jpa.common.BaseService
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.repository.LneQuestRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory


@Service
@Transactional(readOnly = true)
class LneQuestService(val lneQuestRepository: LneQuestRepository,
                      @Qualifier("jpaKorbitEntityManagerFactory") val jpaKorbitEntityManagerFactory: EntityManagerFactory):
    BaseService() {

    lateinit var em : EntityManager

    init {
        em = jpaKorbitEntityManagerFactory.createEntityManager()
    }

    @Transactional
    fun addLneQuest(lneQuest: LneQuest): LneQuest? {
        lneQuest.id = null
        return lneQuestRepository.save(lneQuest)
    }

    @Transactional
    fun updateLneQuest(lneQuest: LneQuest): LneQuest? {
        return lneQuestRepository.save(lneQuest)
    }

    fun getLneQuest(id: Long): LneQuest? {
        val quest: LneQuest? = lneQuestRepository.findById(id).get()
        return quest
    }

    fun getLneQuestAllList(pageable: Pageable): Page<LneQuest> {
        val page: Page<LneQuest>  = lneQuestRepository.findAll(pageable)
        return page
    }

    fun getLneQuestList(type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest>? {
        return lneQuestRepository.findAllBy(type, from, to, pageable)
    }

    fun getLneQuestList2(type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest>? {
        return lneQuestRepository.findAllByTypeAndCreatedAtBetween(type, from, to, pageable)
    }

    fun getLneQuestList3(type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest> {

        // JPQL by em.createQuery() : 테이블 명은 Entity Class Name 으로 인식한다.
        val query = em.createQuery(
            "select q from LneQuest q " +
                    " where q.type = :type and :from < q.createdAt and q.createdAt < :to ", LneQuest::class.java
        )
        query.setParameter("type", type)
        query.setParameter("from", from)
        query.setParameter("to", to)
        query.firstResult = pageable.offset.toInt()
        query.maxResults = pageable.pageSize

        val list =  query.resultList
        return PageImpl<LneQuest>(list, pageable, list.size.toLong())
    }

    fun getLneQuestList4(type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest>? {
        return lneQuestRepository.findAllByJpql(type, from, to, pageable)
    }
}
