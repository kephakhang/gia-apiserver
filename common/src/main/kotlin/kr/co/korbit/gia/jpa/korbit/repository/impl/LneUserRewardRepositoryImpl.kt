package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.jpa.common.QuerydslRepositorySupport
import kr.co.korbit.gia.jpa.korbit.model.*
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneUserRewardRepository
import org.springframework.beans.factory.annotation.Qualifier

class LneUserRewardRepositoryImpl(@Qualifier("jpaKorbitQueryFactory") val jpaKorbitQueryFactory: JPAQueryFactory)
    : QuerydslRepositorySupport<LneUserReward>(), CustomLneUserRewardRepository {


    init {
        super.queryFactory = jpaKorbitQueryFactory
    }

    val path: PathBuilder<LneUserReward> = PathBuilder<LneUserReward>(LneUserReward::class.java, "lneUserReward")

}