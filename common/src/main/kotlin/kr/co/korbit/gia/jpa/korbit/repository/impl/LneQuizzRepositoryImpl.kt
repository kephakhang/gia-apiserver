package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.jpa.common.Querydsl4RepositorySupport
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.model.LneQuizz
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuestRepository
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuizzRepository
import org.springframework.beans.factory.annotation.Qualifier

class LneQuizzRepositoryImpl(@Qualifier("jpaKorbitQueryFactory") val jpaKorbitQueryFactory: JPAQueryFactory)
    : Querydsl4RepositorySupport<LneQuizz>(), CustomLneQuizzRepository {


    init {
        super.queryFactory = jpaKorbitQueryFactory
    }

    val path: PathBuilder<LneQuizz> = PathBuilder<LneQuizz>(LneQuizz::class.java, "path")

}