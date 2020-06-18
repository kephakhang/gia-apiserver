package kr.co.korbit.gia.jpa.korbit.service

import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.repository.LneQuestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(readOnly = true)
class LneQuestService(
    val lneQuestRepository: LneQuestRepository
) {


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

    fun getLneQuestList(pageable: Pageable): Page<LneQuest> {
        val page: Page<LneQuest>  = lneQuestRepository.findAll(pageable)
        return page
    }
}