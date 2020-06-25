package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.korbitapi.model.Clients
import kr.co.korbit.gia.jpa.korbitapi.model.WhiteLists
import kr.co.korbit.gia.jpa.korbitapi.repository.ClientsRepository
import kr.co.korbit.gia.jpa.korbitapi.repository.WhiteListsRepository
import kr.co.korbit.gia.jpa.test.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class WhiteListsController(
    val whiteListsRepository: WhiteListsRepository
): BaseController() {

    @GetMapping(baseUri + "/white_lists")
    fun getWhiteListsByIp(@RequestParam("ip") ip: String): WhiteLists? {

        return whiteListsRepository.findByIp(ip)
    }
}