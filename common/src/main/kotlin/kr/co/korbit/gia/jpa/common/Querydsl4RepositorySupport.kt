package kr.co.korbit.gia.jpa.common

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.querydsl.QSort
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository

@NoRepositoryBean
abstract class Querydsl4RepositorySupport<T>() {

    lateinit var queryFactory: JPAQueryFactory

    open fun ordable(sort: Sort, builder: PathBuilder<*>): MutableList<OrderSpecifier<*>> {

        var specifiers: MutableList<OrderSpecifier<*>>? = null

        if (sort is QSort) {
            specifiers = (sort as QSort).orderSpecifiers
        } else {
            specifiers = mutableListOf()
            for (order: Sort.Order in sort) {
                specifiers.addAll(ordable(order as Sort, builder))
            }
        }

        specifiers?.let {
            return it
        }
        return mutableListOf()
    }

    open fun select(): JPAQuery<T> {
        return queryFactory.query() as JPAQuery<T>
    }

//    open fun<T> applyPagination(pageable: Pageable, contentQuery: Function<JPAQueryFactory, JPAQuery> ): Page<T> {
//        val jpaQuery = contentQuery.apply(queryFactory)
//        val content: List<T> = getQuerydsl().applyPagination(pageable,jpaQuery).fetch();
}