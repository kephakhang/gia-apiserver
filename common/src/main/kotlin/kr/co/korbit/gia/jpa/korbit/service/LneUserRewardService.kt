package kr.co.korbit.gia.jpa.korbit.service

import kr.co.korbit.gia.jpa.korbit.model.LneUserReward
import kr.co.korbit.gia.jpa.korbit.repository.LneUserRewardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LneUserRewardService {
    @Autowired
    val lneUserRewardRepository: LneUserRewardRepository? = null

    fun getLneUserReward(id: Long): LneUserReward? {
        lneUserRewardRepository?.let {
            val userReward: LneUserReward? = it.findById(id).get()
            return userReward
        }
        return null
    }
}