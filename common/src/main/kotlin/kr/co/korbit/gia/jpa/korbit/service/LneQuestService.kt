package kr.co.korbit.gia.jpa.korbit.service

import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.repository.LneQuestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LneQuestService {
    @Autowired
    lateinit var lneQuestRepository: LneQuestRepository

    fun getLneQuest(id: Long): LneQuest? {
        val quest: LneQuest? = lneQuestRepository.findById(id).get()
        return quest
    }

    fun getLneQuestList(from: LocalDateTime, to: LocalDateTime, pageable: Pageable): Page<LneQuest> {
        val page: Page<LneQuest>  = lneQuestRepository.findAllByCreatedAtBetween(from, to, pageable)
        return page
    }
}