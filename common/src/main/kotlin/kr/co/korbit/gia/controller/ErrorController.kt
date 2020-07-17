package kr.co.korbit.gia.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.co.korbit.gia.jpa.common.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(value = "error", description = "코빗 error api")
class ErrorController() {

    /*
    @ApiOperation(
        value = "Interceptor Level Error 발생시 호출되는 401 에러 처리",
        nickname = "addLneQuest",
        response = Response::class
    )
    @GetMapping("/error")
    fun error(map: Map<String, Any>): Response? {

        return Response(false, map, null, null, "GET")
    }
    */

}