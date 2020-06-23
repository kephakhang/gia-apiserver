package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.jpa.common.Querydsl4RepositorySupport
import kr.co.korbit.gia.jpa.korbit.model.LneQuizz
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuizzRepository

class CustomLneQuizzRepositoryImpl(
    val jpaKorbitQueryFactory: JPAQueryFactory
): Querydsl4RepositorySupport<LneQuizz>(jpaKorbitQueryFactory), CustomLneQuizzRepository {

    val path: PathBuilder<LneQuizz> = PathBuilder<LneQuizz>(LneQuizz::class.java, "path")

}