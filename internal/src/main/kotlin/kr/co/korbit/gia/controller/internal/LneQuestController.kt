package kr.co.korbit.gia.controller.internal

import io.swagger.annotations.*
import kr.co.korbit.gia.jpa.common.Ok
import kr.co.korbit.gia.jpa.common.Response
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.service.LneQuestService
import kr.co.korbit.gia.jpa.test.model.Session
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.validation.Valid

@RestController
@Api(value = "quest", description = "코빗 quest api")
class LneQuestController(
    val questService: LneQuestService
): BaseController() {

    @ApiOperation(
        value = "",
        nickname = "addLneQuest",
        notes = "신규 quest 정보를 테이블에 추가하기",
        response = Response::class
    )
    @ApiResponses(
        value = [ApiResponse(
            code = 201,
            message = "추가된 quest 정보 응답",
            response = Response::class
        )]
    )
    @PostMapping(baseUri + "/quest")
    fun addLneQuest(@Valid @RequestBody quest: LneQuest, session: Session): Response? {

        return Ok(questService.addLneQuest(quest))
    }


    @ApiOperation(
        value = "",
        nickname = "getLneQuest",
        notes = "quest 정보를 수정(update)한다",
        response = Response::class
    )
    @ApiResponses(
        value = [ApiResponse(
            code = 201,
            message = "수정된 quest 정보 응답",
            response = Response::class
        )]
    )
    @PutMapping(baseUri + "/quest")
    fun updateLneQuest(@Valid @RequestBody quest: LneQuest, session: Session): Response? {

        return Ok(questService.updateLneQuest(quest))
    }

    @ApiOperation(
        value = "quest 정보 가져오기",
        nickname = "getLneQuest: quest 정보를 id(primary key)로 가져오기",
        notes = "quest 정보를 id(primary key)로 가져오기",
        response = Response::class)
    @GetMapping(baseUri + "/quest")
    fun getLneQuest(
        @ApiParam(name = "id", type = "Number",value = "lne_quest.id 고유 키 아이디 값", example = "1", required = true)
        @RequestParam("id", required = true) id: Long,
        session: Session): Response? {

        return Ok(questService.getLneQuest(id))
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
    @GetMapping(baseUri + "/quest/list")
    fun getLneQuestList(@RequestParam("type", required = false) type: String?,
                        @RequestParam("from", required = false) from: LocalDateTime?,
                        @RequestParam("to", required = false) to: LocalDateTime?, pageable: Pageable, session: Session): Response? {

        return Ok(questService.getLneQuestList(type, from, to, pageable))
    }

    @GetMapping(baseUri + "/quest/list2")
    fun getLneQuestList2(@RequestParam("type", required = false) type: String?,
                        @RequestParam("from", required = false) from: LocalDateTime?,
                        @RequestParam("to", required = false) to: LocalDateTime?, pageable: Pageable, session: Session): Response? {

        return Ok(questService.getLneQuestList2(type, from, to, pageable))
    }
}