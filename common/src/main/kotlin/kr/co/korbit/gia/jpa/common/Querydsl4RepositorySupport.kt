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

        lateinit var specifiers: MutableList<OrderSpecifier<*>>

        if (sort is QSort) {
        specifiers = sort.orderSpecifiers
        } else {
            specifiers = mutableListOf()
            for (order: Sort.Order in sort) {
                //ToDo : This cast can never succeed ???????????? fix it
                specifiers.addAll(ordable(order as Sort, builder))
            }
        }

        return specifiers
    }

    open fun select(): JPAQuery<T> {
        //ToDo : Unchecked cast : JPAQuery<*>! to JPAQuery<T> ??? fix it
        return queryFactory.query() as JPAQuery<T>
    }

//    open fun<T> applyPagination(pageable: Pageable, contentQuery: Function<JPAQueryFactory, JPAQuery> ): Page<T> {
//        val jpaQuery = contentQuery.apply(queryFactory)
//        val content: List<T> = getQuerydsl().applyPagination(pageable,jpaQuery).fetch();
}