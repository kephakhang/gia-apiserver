package kr.co.korbit.gia.jpa.korbit.model

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.annotations.ApiModel
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.springframework.validation.annotation.Validated
import java.time.LocalDateTime
import javax.annotation.Generated
import javax.persistence.*
import kr.co.korbit.gia.jpa.common.AbstractJpaPersistable as AbstractJpaPersistable1

/**
 * subscription information
 */
@ApiModel(description = "Korbit Quest Event Entity Model")
@Validated
@Generated(
    value = ["kr.co.korbit"],
    date = "2020-06-16T17:24:48.045401+09:00[Asia/Seoul]"
)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lneQuest")
    var lneQuizzList: MutableList<LneQuizz> = mutableListOf()
): AbstractJpaPersistable1() {
    
}