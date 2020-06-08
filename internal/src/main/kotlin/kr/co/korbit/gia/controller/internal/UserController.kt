package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.jpa.test.model.Users
import kr.co.korbit.gia.jpa.test.service.UsersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    val usersService: UsersService? = null

    @GetMapping
    fun getUser(@RequestParam("id") id: Long): Users? {

        return usersService?.getUser(id)
    }
}