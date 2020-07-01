package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.jpa.common.Ok
import kr.co.korbit.gia.service.korbit.LneQuizService
import kr.co.korbit.gia.jpa.korbit.model.dto.Session
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LneQuizController(
    val quizService: LneQuizService
): BaseController() {

    @GetMapping(baseUri + "/quiz")
    fun getLneQuiz(@RequestParam("id") id: Long, session: Session): Any? {

        return Ok(quizService.getLneQuiz(id))
    }
}