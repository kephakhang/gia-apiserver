package kr.co.korbit.gia.jpa.admin.repository.impl

import kr.co.korbit.gia.jpa.admin.model.AdminUser
import kr.co.korbit.gia.jpa.admin.repository.custom.CustomAdminUserRepository
import kr.co.korbit.gia.jpa.common.Querydsl4RepositorySupport
import org.springframework.jdbc.core.JdbcTemplate
import javax.persistence.EntityManager
import kotlin.reflect.KClass

class AdminUserRepositoryImpl(
    val jpaAdminEntityManager: EntityManager,
    val adminJdbcTemplate: JdbcTemplate
)  : /*Querydsl4RepositorySupport<AdminUser>(
        jpaAdminEntityManager,
        adminJdbcTemplate,
        AdminUser::class as KClass<Any>
    ), */ CustomAdminUserRepository {

}