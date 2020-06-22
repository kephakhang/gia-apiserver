package kr.co.korbit.gia.jpa.korbit.repository.custom

import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface CustomLneQuestRepository {
    fun findAllByCreatedAtBetween(type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest>
}