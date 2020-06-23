package kr.co.korbit.gia.jpa.korbit.service

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.common.WhereBuilder
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuizz
import kr.co.korbit.gia.jpa.korbit.repository.LneQuestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
@Transactional(readOnly = true)
class LneQuestService(val lneQuestRepository: LneQuestRepository) {

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

    fun getLneQuestList(type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest> {
        return lneQuestRepository.findAllBy(type, from, to, pageable)
    }
}