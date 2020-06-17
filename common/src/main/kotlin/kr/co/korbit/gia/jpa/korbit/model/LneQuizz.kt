package kr.co.korbit.gia.jpa.korbit.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import kr.co.korbit.gia.jpa.common.AbstractJpaPersistable
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "LneQuizzes")
@Table(name = "korbit.lne_quizzes")
@Cacheable
@JsonInclude(JsonInclude.Include.NON_NULL)
open class LneQuizz(


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    var  lneQuest: LneQuest,

//    @Column(name = "quest_id")
//    var questId: Long,

    var  type: String,
    var  title: String,
    var  description: String? = null,
    var  answers: String? = null,

    @Column(name = "correct_answer")
    var  correctAnswer: String,

    @Column(name = "deleted_at")
    var  deletedAt: LocalDateTime? = null
): AbstractJpaPersistable() {
}