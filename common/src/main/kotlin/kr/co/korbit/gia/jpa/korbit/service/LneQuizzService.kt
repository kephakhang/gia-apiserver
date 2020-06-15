package kr.co.korbit.gia.jpa.korbit.service

import kr.co.korbit.gia.jpa.korbit.model.LneQuizz
import kr.co.korbit.gia.jpa.korbit.repository.LneQuizzRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LneQuizzService {
    @Autowired
    val lneQuizzRepository: LneQuizzRepository? = null

    fun getLneQuizz(id: Long): LneQuizz? {
        lneQuizzRepository?.let {
            val quizz: LneQuizz? = it.findById(id).get()
            return quizz
        }
        return null
    }
}