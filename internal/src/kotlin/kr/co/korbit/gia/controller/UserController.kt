package kr.co.korbit.gia.controller

import kr.co.korbit.gia.jpa.korbitapi.model.Clients
import kr.co.korbit.gia.jpa.korbitapi.repository.ClientsRepository
import kr.co.korbit.gia.jpa.test.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/clients")
class UserController {

    @Autowired
    val userService: UserService? = null

    @GetMapping
    fun getClientsByKey(@RequestParam("key") key: String): Clients? {

        clientsRepository?.let {
            return it.findByKey(key)
        }
        return null
    }
}