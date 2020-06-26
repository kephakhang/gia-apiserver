package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.jpa.korbitapi.model.WhiteLists
import kr.co.korbit.gia.jpa.korbitapi.repository.WhiteListsRepository
import org.springframework.web.bind.annotation.GetMapping
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