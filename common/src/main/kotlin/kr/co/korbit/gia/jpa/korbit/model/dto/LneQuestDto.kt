package kr.co.korbit.gia.jpa.korbit.model.dto


import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import java.time.LocalDateTime
import kotlin.properties.Delegates

/**
 * subscription information
 */
class LneQuestDto() {


    lateinit var type: String
    lateinit var title: String
    var description: String? = null
    var userConstraints: String = "{}"
    var rewardTotal by Delegates.notNull<Double>()
    var rewardRemain by Delegates.notNull<Double>()
    var rewardAmount by Delegates.notNull<Double>()
    lateinit var thumbnailUrl: String
    lateinit var startAt: LocalDateTime
    lateinit var endAt: LocalDateTime

    constructor(lneQuest: LneQuest): this() {
        type = lneQuest.type
        title = lneQuest.title
        description = lneQuest.description
        userConstraints = lneQuest.userConstraints
        rewardTotal = lneQuest.rewardTotal
        rewardRemain = lneQuest.rewardRemain
        rewardAmount = lneQuest.rewardAmount
        thumbnailUrl = lneQuest.thumbnailUrl
        startAt = lneQuest.startAt
        endAt = lneQuest.endAt
    }
}