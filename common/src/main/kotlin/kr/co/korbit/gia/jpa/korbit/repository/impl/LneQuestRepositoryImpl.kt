package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.QueryResults
import com.querydsl.core.types.dsl.PathBuilder
import kr.co.korbit.gia.jpa.common.Querydsl4RepositorySupport
import kr.co.korbit.gia.jpa.common.WhereBuilder
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuest.*
import kr.co.korbit.gia.jpa.korbit.model.QLneQuiz.*
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuestRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.*
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import kotlin.reflect.KClass

class LneQuestRepositoryImpl(
    val jpaKorbitEntityManager: EntityManager,
    val korbitJdbcTemplate: JdbcTemplate
)
    : Querydsl4RepositorySupport<LneQuest>(
        jpaKorbitEntityManager,
        korbitJdbcTemplate,
        LneQuest::class as KClass<Any>
    ), CustomLneQuestRepository {

    //val path: PathBuilder<LneQuest> = PathBuilder<LneQuest>(LneQuest::class.java, "lneQuest")
    //val lneQuest: QLneQuest = QLneQuiz("lneQuest")

    override fun findAllBy(type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest> {

        val jpql =
            selectFrom(lneQuest)
                .where(
                    WhereBuilder()
                        .optionalAnd(from, { lneQuest.createdAt.after(from) })
                        .optionalAnd(to, { lneQuest.createdAt.before(to) })
                        .optionalAnd(type, { lneQuest.type.eq(type) })
                )
                .orderBy(*ordable(pageable.sort).toTypedArray())

        val countJpql =
                selectFrom(lneQuest)
                .where(
                    WhereBuilder()
                        .optionalAnd(from, { lneQuest.createdAt.after(from) })
                        .optionalAnd(to, { lneQuest.createdAt.before(to) })
                        .optionalAnd(type, { lneQuest.type.eq(type) })
                )

        return applyPagination(pageable, jpql, countJpql)
    }
}