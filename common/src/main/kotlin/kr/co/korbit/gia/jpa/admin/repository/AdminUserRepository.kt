package kr.co.korbit.gia.jpa.admin.repository

import kr.co.korbit.gia.jpa.admin.model.AdminUser
import kr.co.korbit.gia.jpa.admin.repository.custom.CustomAdminUserRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional(transactionManager = "jpaAdminTransactionManager")
interface AdminUserRepository : JpaRepository<AdminUser, Long>, CustomAdminUserRepository {
    override fun findById(id: Long): Optional<AdminUser>
    fun findByEmail(email: String): Optional<AdminUser>
    fun findByPhone(phone: String): Optional<AdminUser>
}
