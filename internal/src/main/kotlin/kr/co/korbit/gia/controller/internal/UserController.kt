package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.annotation.SkipSessionCheck
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.common.Ok
import kr.co.korbit.gia.jpa.common.Response
import kr.co.korbit.gia.jpa.test.model.Session
import kr.co.korbit.gia.jpa.test.model.User
import kr.co.korbit.gia.jpa.test.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @Autowired
    val userService: UserService? = null

    @GetMapping("/user")
    fun getUser(@RequestParam("id") id: Long): Any? {

        //Env.objectMapper.writeValueAsString(session)

        return Ok(userService?.getUser(id))
    }
}