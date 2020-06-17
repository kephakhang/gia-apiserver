package kr.co.korbit.gia.jpa.korbit.service

import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.korbit.model.LneUserReward
import kr.co.korbit.gia.jpa.korbit.repository.LneUserRewardRepository
import org.springframework.stereotype.Service

@Service
class LneUserRewardService(
    val lneUserRewardRepository: LneUserRewardRepository = Env.appContext.getBean(LneUserRewardRepository::class.java)
) {

    fun getLneUserReward(id: Long): LneUserReward? {
        val userReward: LneUserReward? = lneUserRewardRepository.findById(id).get()
        return userReward
    }
}