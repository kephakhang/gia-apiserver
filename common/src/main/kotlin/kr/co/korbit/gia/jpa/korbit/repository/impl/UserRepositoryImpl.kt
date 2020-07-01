package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.jpa.common.QuerydslRepositorySupport
import kr.co.korbit.gia.jpa.korbit.model.User
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomUserRepository
import org.springframework.beans.factory.annotation.Qualifier

class UserRepositoryImpl(@Qualifier("jpaKorbitQueryFactory") val jpaKorbitQueryFactory: JPAQueryFactory)
    : QuerydslRepositorySupport<User>(), CustomUserRepository {


    init {
        super.queryFactory = jpaKorbitQueryFactory
    }

    val path: PathBuilder<User> = PathBuilder<User>(User::class.java, "user")

}