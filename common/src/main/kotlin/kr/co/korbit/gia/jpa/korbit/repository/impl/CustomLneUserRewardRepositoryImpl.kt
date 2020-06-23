package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.jpa.common.Querydsl4RepositorySupport
import kr.co.korbit.gia.jpa.korbit.model.*
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneUserRewardRepository

class CustomLneUserRewardRepositoryImpl(
    val jpaKorbitQueryFactory: JPAQueryFactory
): Querydsl4RepositorySupport<LneUserReward>(jpaKorbitQueryFactory), CustomLneUserRewardRepository {

    val path: PathBuilder<LneQuizz> = PathBuilder<LneQuizz>(LneQuizz::class.java, "path")

}