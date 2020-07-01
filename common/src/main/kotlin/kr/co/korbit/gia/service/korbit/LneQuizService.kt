package kr.co.korbit.gia.service.korbit

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.korbit.model.LneQuiz
import kr.co.korbit.gia.jpa.korbit.repository.LneQuizRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class LneQuizService(val lneQuizRepository: LneQuizRepository) {



    fun getLneQuiz(id: Long): LneQuiz? {

        val quizz: LneQuiz? = lneQuizRepository.findById(id).get()
        return quizz
    }
}