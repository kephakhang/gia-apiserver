package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.jpa.common.Querydsl4RepositorySupport
import kr.co.korbit.gia.jpa.korbit.model.User
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomUserRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import javax.persistence.EntityManager
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class UserRepositoryImpl(val jpaKorbitEntityManager: EntityManager,
                         val korbitJdbcTemplate: JdbcTemplate
) : Querydsl4RepositorySupport<User>(
        jpaKorbitEntityManager,
        korbitJdbcTemplate,
         User::class as KClass<Any>
        ), CustomUserRepository {


}