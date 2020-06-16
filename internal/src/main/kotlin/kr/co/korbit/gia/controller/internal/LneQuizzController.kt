package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.annotation.SkipSessionCheck
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.common.Ok
import kr.co.korbit.gia.jpa.common.Response
import kr.co.korbit.gia.jpa.korbit.service.LneQuestService
import kr.co.korbit.gia.jpa.korbit.service.LneQuizzService
import kr.co.korbit.gia.jpa.test.model.Session
import kr.co.korbit.gia.jpa.test.model.User
import kr.co.korbit.gia.jpa.test.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class LneQuizzController {

    @Autowired
    lateinit var lneQuizzService: LneQuizzService

    @GetMapping("/quizz")
    fun getLneQuizz(@RequestParam("id") id: Long, session: Session): Any? {

        return Ok(lneQuizzService.getLneQuizz(id))
    }
}