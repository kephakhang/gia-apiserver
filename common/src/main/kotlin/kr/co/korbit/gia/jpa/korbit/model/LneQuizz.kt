package kr.co.korbit.gia.jpa.korbit.model

import com.fasterxml.jackson.annotation.JsonInclude
import kr.co.korbit.gia.jpa.common.AbstractJpaPersistable
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "LneQuizzes")
@Table(name = "korbit.lne_quizzes")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
open class LneQuizz(

    @ManyToOne
    @JoinColumn(name = "quest_id")
    var  quest: LneQuest,

    var  type: String,
    var  title: String,
    var  description: String? = null,
    var  answers: String? = null,

    var  correctAnswer: String,
    var  deletedAt: LocalDateTime? = null
): AbstractJpaPersistable() {
}