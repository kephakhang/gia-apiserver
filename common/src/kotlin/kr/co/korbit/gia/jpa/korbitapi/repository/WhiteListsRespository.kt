package kr.co.korbit.gia.jpa.korbitapi.repository

import kr.co.korbit.gia.jpa.korbitapi.model.Clients
import kr.co.korbit.gia.jpa.korbitapi.model.WhiteLists
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "jpaKorbitApiTransactionManager")
interface WhiteListsRepository : JpaRepository<WhiteLists, String> {
    fun findByIp(ip: String): WhiteLists
    fun findAllBy() : List<WhiteLists>
}