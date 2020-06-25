package kr.co.korbit.gia.jpa.korbit.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import javax.persistence.*
import kr.co.korbit.gia.jpa.common.AbstractJpaPersistable as AbstractJpaPersistable1

/**
 * subscription information
 */
@Entity(name = "LneQuests")
@Table(name = "korbit.lne_quests")
@Cacheable
@JsonInclude(JsonInclude.Include.NON_NULL)
class LneQuest (


    var type: String,
    var title: String,
    var description: String?,

    @Column(name = "user_constraints")
    var userConstraints: String = "{}",

    @Column(name = "reward_total")
    var rewardTotal: Double,

    @Column(name = "reward_remain")
    var rewardRemain: Double,

    @Column(name = "reward_amount")
    var rewardAmount: Double,

    @Column(name = "reward_currency")
    var rewardCurrency: String,

    @Column(name = "thumbnail_url")
    var thumbnailUrl: String,

    @Column(name = "start_at")
    var startAt: LocalDateTime,

    @Column(name = "end_at")
    var endAt: LocalDateTime,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "quest", cascade = [CascadeType.ALL])
    var quizzList: MutableList<LneQuizz> = mutableListOf()


): AbstractJpaPersistable1() {

    // 연관관계 추가 method 는 만들어 놓는게 편리하다.
    fun addQuizz(quizz: LneQuizz) {
        quizz.quest = this
        quizzList.add(quizz)
    }
}