package kr.co.korbit.gia.controller.internal

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController: BaseController() {

    @GetMapping(baseUri + "/hello")
    fun hello(): String {
        return "Hello"
    }
}
