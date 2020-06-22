package kr.co.korbit.gia.jpa.korbit.service

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.korbit.model.LneUserReward
import kr.co.korbit.gia.jpa.korbit.repository.LneUserRewardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class LneUserRewardService(val lneUserRewardRepository: LneUserRewardRepository) {

    fun getLneUserReward(id: Long): LneUserReward? {
        val userReward: LneUserReward? = lneUserRewardRepository.findById(id).get()
        return userReward
    }
}