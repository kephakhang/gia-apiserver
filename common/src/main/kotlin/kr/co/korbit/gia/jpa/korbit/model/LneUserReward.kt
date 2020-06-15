package kr.co.korbit.gia.jpa.korbit.model

import com.fasterxml.jackson.annotation.JsonInclude
import kr.co.korbit.gia.jpa.common.AbstractJpaPersistable
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.Type
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "LneUserRewards")
@Table(name = "korbit.lne_user_rewards")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
class LneUserReward(
    var user_id: Long,

    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "quest_id", referencedColumnName = "id")
    var lneQuest: LneQuest,

    var currency: String,
    var amount: Double,

    @Type(type = "yes_no")
    var paid: Boolean,
    var paidAt: LocalDateTime,
    var rewardedAmountInKrw: Double = 0.0,
    var paidAmountInKrw: Double = 0.0,
    var deletedAt: LocalDateTime? = null
): AbstractJpaPersistable() {
    
}