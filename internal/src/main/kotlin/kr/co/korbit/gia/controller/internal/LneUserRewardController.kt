package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.jpa.common.Ok
import kr.co.korbit.gia.service.korbit.LneUserRewardService
import kr.co.korbit.gia.jpa.test.model.Session
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LneUserRewardController(
    val userRewardService: LneUserRewardService
): BaseController() {

    @GetMapping(baseUri + "/user_reward")
    fun getLneQuizz(@RequestParam("id") id: Long, session: Session): Any? {

        return Ok(userRewardService.getLneUserReward(id))
    }
}