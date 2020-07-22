package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.types.dsl.PathBuilder
import kr.co.korbit.gia.jpa.common.Querydsl4RepositorySupport
import kr.co.korbit.gia.jpa.korbit.model.*
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneUserRewardRepository
import org.springframework.jdbc.core.JdbcTemplate
import javax.persistence.EntityManager
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class LneUserRewardRepositoryImpl(
    val jpaKorbitEntityManager: EntityManager,
    val korbitJdbcTemplate: JdbcTemplate
    ) : Querydsl4RepositorySupport<LneQuest>(
        jpaKorbitEntityManager,
        korbitJdbcTemplate,
        LneQuest::class as KClass<Any>
    ), CustomLneUserRewardRepository {


    val path: PathBuilder<LneUserReward> = PathBuilder<LneUserReward>(LneUserReward::class.java, "lneUserReward")

}