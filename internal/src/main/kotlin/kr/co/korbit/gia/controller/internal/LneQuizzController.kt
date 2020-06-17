package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.common.Ok
import kr.co.korbit.gia.jpa.korbit.service.LneQuestService
import kr.co.korbit.gia.jpa.korbit.service.LneQuizzService
import kr.co.korbit.gia.jpa.test.model.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LneQuizzController(
    val lneQuizzService: LneQuizzService = Env.appContext.getBean(LneQuizzService::class.java)
): BaseController() {

    @GetMapping(baseUri + "/quizz")
    fun getLneQuizz(@RequestParam("id") id: Long, session: Session): Any? {

        return Ok(lneQuizzService.getLneQuizz(id))
    }
}