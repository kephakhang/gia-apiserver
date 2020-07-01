package kr.co.korbit.gia.jpa.common

import com.querydsl.core.types.EntityPath
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport
import org.springframework.data.jpa.repository.support.Querydsl
import org.springframework.data.querydsl.SimpleEntityPathResolver
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.support.PageableExecutionUtils
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.util.Assert
import java.util.function.Function
import javax.annotation.PostConstruct
import javax.persistence.EntityManager
import kotlin.reflect.KClass


/**
 * Querydsl 4.x 버전에 맞춘 Querydsl 지원 라이브러리
 *
 * @author Younghan Kim
 * @see org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
 */
@NoRepositoryBean
abstract class Querydsl4RepositorySupport<T>(val entityManager: EntityManager, val jdbcTemplate: JdbcTemplate, val domainClass: KClass<Any>) {
    lateinit var querydsl: Querydsl
    lateinit var queryFactory: JPAQueryFactory
    lateinit var builder: PathBuilder<*>
    protected val batchSize = 300

    init {
        Assert.notNull(entityManager, "EntityManager must not be null!")
        val entityInformation: JpaEntityInformation<*, *> =
            JpaEntityInformationSupport.getEntityInformation<T>(domainClass.java as Class<T>, entityManager)
        val resolver = SimpleEntityPathResolver.INSTANCE
        val path: EntityPath<*> = resolver.createPath<T>(entityInformation.javaType as Class<T>)
        builder = PathBuilder(path.type, path.metadata)
        querydsl = Querydsl(
            entityManager,
            builder
        )
        queryFactory = JPAQueryFactory(entityManager)
    }

    //ref : https://stackoverflow.com/questions/13072378/how-can-i-convert-a-spring-data-sort-to-a-querydsl-orderspecifier
    open fun ordable(sort: Sort): MutableList<OrderSpecifier<*>> {

        val specifiers: MutableList<OrderSpecifier<*>> = mutableListOf()

        for (o in sort) {

            specifiers.add(
                OrderSpecifier<Comparable<Any>>(
                    if (o.isAscending) Order.ASC else Order.DESC,
                    builder[o.property] as Expression<Comparable<Any>>
                )
            )
        }

        return specifiers
    }

    @PostConstruct
    fun validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!")
        Assert.notNull(querydsl, "Querydsl must not be null!")
        Assert.notNull(queryFactory, "QueryFactory must not be null!")
    }

    protected fun select(): JPAQuery<T> {
        //ToDo : Unchecked cast : JPAQuery<*>! to JPAQuery<T> ??? fix it
        return queryFactory.query() as JPAQuery<T>
    }

    protected fun select(expr: Expression<T>?): JPAQuery<T> {
        return queryFactory.select(expr)
    }

    protected fun selectFrom(from: EntityPath<T>?): JPAQuery<T> {
        return queryFactory.selectFrom(from)
    }

    protected fun applyPagination(
        pageable: Pageable?,
        jpaQuery: JPAQuery<*>
    ): Page<T> {
        val content: List<T> = querydsl.applyPagination(
            pageable,
            jpaQuery
        ).fetch() as List<T>
        return PageableExecutionUtils.getPage(content, pageable) { jpaQuery.fetchCount() }
    }

    protected fun applyPagination(
        pageable: Pageable?,
        jpaQuery: JPAQuery<*>,
        countQuery: JPAQuery<*>
    ): Page<T> {
        val content: List<T> = querydsl.applyPagination(
            pageable,
            jpaQuery
        ).fetch() as List<T>
        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchCount() }
    }
}