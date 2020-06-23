package kr.co.korbit.gia.jpa.korbit.repository.custom

import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

open interface CustomLneQuestRepository {
    fun findAllBy(type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest>
}