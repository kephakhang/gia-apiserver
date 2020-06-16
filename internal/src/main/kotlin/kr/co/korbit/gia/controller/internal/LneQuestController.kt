package kr.co.korbit.gia.controller.internal

import io.swagger.annotations.*
import kr.co.korbit.gia.jpa.common.Ok
import kr.co.korbit.gia.jpa.common.Response
import kr.co.korbit.gia.jpa.korbit.service.LneQuestService
import kr.co.korbit.gia.jpa.test.model.Session
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@Api(value = "quest", description = "코빗 quest api")
class LneQuestController {

    @Autowired
    lateinit var lneQuestService: LneQuestService

    //    @RequestMapping(value = ["/streams"], produces = ["application/json"], method = [RequestMethod.POST])
    @ApiOperation(
        value = "",
        nickname = "getLneQuest",
        notes = "quest 정보를 id(primary key)로 가져오기",
        response = Response::class
    )
    @ApiResponses(
        value = [ApiResponse(
            code = 201,
            message = "quest 정보 응답",
            response = Response::class
        )]
    )
    @GetMapping("/quest")
    fun getLneQuest(@ApiParam(
                        value = "quest 의 id(primary key)",
                        required = true
                    ) @RequestParam("id", required = true) id: Long, session: Session): Response? {

        return Ok(lneQuestService.getLneQuest(id))
    }


    @ApiOperation(
        value = "",
        nickname = "getLneQuestList",
        notes = "요청기간(from ~ to) 동안의 quest 리스트를 가져오기 (Pageable)",
        response = Response::class
    )
    @ApiResponses(
        value = [ApiResponse(
            code = 201,
            message = "quest 리스트 정보 응답",
            response = Response::class
        )]
    )
    @GetMapping("/quest/list")
    fun getLneQuestList(@NotNull @ApiParam(
                            value = "요청기간의 시작 시각",
                            required = true
                        ) @RequestParam("from", required = true) from: LocalDateTime,
                        @NotNull @ApiParam(
                            value = "요청기간의 종료 시각",
                            required = true
                        ) @RequestParam("to", required = true) to: LocalDateTime, pageable: Pageable, session: Session): Response? {

        return Ok(lneQuestService.getLneQuestList(from, to, pageable))
    }
}