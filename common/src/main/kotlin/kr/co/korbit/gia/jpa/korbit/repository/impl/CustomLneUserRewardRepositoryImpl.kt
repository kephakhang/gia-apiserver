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
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneUserRewardRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

class CustomLneUserRewardRepositoryImpl(
    val jpaKorbitQueryFactory: JPAQueryFactory
): BaseRepository(jpaKorbitQueryFactory), CustomLneUserRewardRepository {

    val userRewardPath: PathBuilder<LneQuizz> = PathBuilder<LneQuizz>(LneQuizz::class.java, "userReward")
    val userReward: QLneQuizz = QLneQuizz("userReward")

}