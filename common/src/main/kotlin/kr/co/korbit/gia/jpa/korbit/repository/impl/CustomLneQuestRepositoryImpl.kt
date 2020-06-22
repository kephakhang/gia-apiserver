package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.jpa.common.WhereBuilder
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuizz
import kr.co.korbit.gia.jpa.common.BaseRepository
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuestRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

class CustomLneQuestRepositoryImpl(
    val jpaKorbitQueryFactory: JPAQueryFactory
): BaseRepository(jpaKorbitQueryFactory), CustomLneQuestRepository {

    val questPath: PathBuilder<LneQuest> = PathBuilder<LneQuest>(LneQuest::class.java, "quest")
    val quest: QLneQuest = QLneQuest("quest")
    val quizz: QLneQuizz = QLneQuizz("quizz")

    override fun findAllByCreatedAtBetween(type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest> {
        val sql =
            from(quest)
                .join(quest.quizzList, quizz)
                .where( WhereBuilder()
                    .optionalAnd(from, quest.createdAt.after(from))
                    .optionalAnd(to, quest.createdAt.before(to))
                    .optionalAnd(type, quest.type.eq(type)) )
                .orderBy(*ordable(pageable.sort,questPath).toTypedArray())
                .offset(pageable.offset)
                .limit(pageable.pageSize as Long)
        val questList: List<LneQuest> = sql.fetch()
        return PageImpl<LneQuest>(questList, pageable, sql.fetchCount())
    }

}