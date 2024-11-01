package kr.co.korbit.gia.controller.internal

import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.korbitapi.model.Clients
import kr.co.korbit.gia.jpa.korbitapi.repository.ClientsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ClientsController(
    val clientsRepository: ClientsRepository
): BaseController() {



    @GetMapping(baseUri + "/client")
    fun getClientsByKey(@RequestParam("key") key: String): Clients? {
        return clientsRepository.findByKey(key)
    }
}