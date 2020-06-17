package kr.co.korbit.gia.jpa.korbit.service

import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.korbit.model.LneQuizz
import kr.co.korbit.gia.jpa.korbit.repository.LneQuizzRepository
import org.springframework.stereotype.Service

@Service
class LneQuizzService(
    val lneQuizzRepository: LneQuizzRepository = Env.appContext.getBean(LneQuizzRepository::class.java)
) {



    fun getLneQuizz(id: Long): LneQuizz? {

        val quizz: LneQuizz? = lneQuizzRepository.findById(id).get()
        return quizz
    }
}