package kr.co.korbit.gia.jpa.korbit.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import kr.co.korbit.gia.jpa.common.BasePersistable
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "LneQuiz")
@Table(name = "korbit.lne_quizzes")
@Cacheable
@JsonInclude(JsonInclude.Include.NON_NULL)
open class LneQuiz(


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    var  quest: LneQuest,

    var  type: String,
    var  title: String,
    var  description: String? = null,
    var  answers: String? = null,

    @Column(name = "correct_answer")
    var  correctAnswer: String,

    @Column(name = "deleted_at")
    var  deletedAt: LocalDateTime? = null
): BasePersistable() {
}