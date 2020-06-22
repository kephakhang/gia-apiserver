package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.jpa.common.WhereBuilder
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuizz
import kr.co.korbit.gia.jpa.common.BaseRepository
import kr.co.korbit.gia.jpa.korbit.model.LneQuizz
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuestRepository
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuizzRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

class CustomLneQuizzRepositoryImpl(
    val jpaKorbitQueryFactory: JPAQueryFactory
): BaseRepository(jpaKorbitQueryFactory), CustomLneQuizzRepository {

    val quizzPath: PathBuilder<LneQuizz> = PathBuilder<LneQuizz>(LneQuizz::class.java, "quizz")
    val quizz: QLneQuizz = QLneQuizz("quizz")

}