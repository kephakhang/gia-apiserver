package kr.co.korbit.gia.controller

import kr.co.korbit.gia.jpa.korbitapi.model.Clients
import kr.co.korbit.gia.jpa.korbitapi.model.WhiteLists
import kr.co.korbit.gia.jpa.korbitapi.repository.ClientsRepository
import kr.co.korbit.gia.jpa.korbitapi.repository.WhiteListsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/white_lists")
class WhiteListsController {

    @Autowired
    val whiteListsRepository: WhiteListsRepository? = null

    @GetMapping
    fun getWhiteListsByIp(@RequestParam("ip") ip: String): WhiteLists? {

        whiteListsRepository?.let {
            return it.findByIp(ip)
        }
        return null
    }
}