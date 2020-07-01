package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.jpa.common.QuerydslRepositorySupport
import kr.co.korbit.gia.jpa.korbit.model.LneQuiz
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuizRepository
import org.springframework.beans.factory.annotation.Qualifier

class LneQuizRepositoryImpl(@Qualifier("jpaKorbitQueryFactory") val jpaKorbitQueryFactory: JPAQueryFactory)
    : QuerydslRepositorySupport<LneQuiz>(), CustomLneQuizRepository {


    init {
        super.queryFactory = jpaKorbitQueryFactory
    }

    val path: PathBuilder<LneQuiz> = PathBuilder<LneQuiz>(LneQuiz::class.java, "lneQuiz")

}