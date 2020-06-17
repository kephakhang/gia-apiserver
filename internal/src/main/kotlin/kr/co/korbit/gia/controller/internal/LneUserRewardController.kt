package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.common.Ok
import kr.co.korbit.gia.jpa.korbit.service.LneQuizzService
import kr.co.korbit.gia.jpa.korbit.service.LneUserRewardService
import kr.co.korbit.gia.jpa.test.model.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LneUserRewardController(
    val lneUserRewardService: LneUserRewardService = Env.appContext.getBean(LneUserRewardService::class.java)
): BaseController() {

    @GetMapping(baseUri + "/user_reward")
    fun getLneQuizz(@RequestParam("id") id: Long, session: Session): Any? {

        return Ok(lneUserRewardService.getLneUserReward(id))
    }
}