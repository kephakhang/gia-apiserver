package kr.co.korbit.gia.jpa.korbitapi.repository

import kr.co.korbit.gia.jpa.korbitapi.model.Clients
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "jpaKorbitApiTransactionManager")
interface ClientsRepository : JpaRepository<Clients, String> {
    fun findByKey(key: String): Clients
}