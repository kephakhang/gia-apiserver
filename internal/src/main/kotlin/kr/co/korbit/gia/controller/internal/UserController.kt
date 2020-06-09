package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.jpa.test.model.User
import kr.co.korbit.gia.jpa.test.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    val userService: UserService? = null

    @GetMapping
    fun getUser(@RequestParam("id") id: Long): User? {

        return userService?.getUser(id)
    }
}