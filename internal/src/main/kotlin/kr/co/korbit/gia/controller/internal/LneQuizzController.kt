package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.jpa.common.Ok
import kr.co.korbit.gia.service.korbit.LneQuizzService
import kr.co.korbit.gia.jpa.test.model.Session
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LneQuizzController(
    val quizzService: LneQuizzService
): BaseController() {

    @GetMapping(baseUri + "/quizz")
    fun getLneQuizz(@RequestParam("id") id: Long, session: Session): Any? {

        return Ok(quizzService.getLneQuizz(id))
    }
}