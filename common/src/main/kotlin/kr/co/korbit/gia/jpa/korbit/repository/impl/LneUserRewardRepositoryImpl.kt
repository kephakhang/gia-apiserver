package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.jpa.common.Querydsl4RepositorySupport
import kr.co.korbit.gia.jpa.korbit.model.*
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuestRepository
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneUserRewardRepository
import org.springframework.beans.factory.annotation.Qualifier

class LneUserRewardRepositoryImpl(@Qualifier("jpaKorbitQueryFactory") val jpaKorbitQueryFactory: JPAQueryFactory)
    : Querydsl4RepositorySupport<LneUserReward>(), CustomLneUserRewardRepository {


    init {
        super.queryFactory = jpaKorbitQueryFactory
    }

    val path: PathBuilder<LneQuizz> = PathBuilder<LneQuizz>(LneQuizz::class.java, "path")

}