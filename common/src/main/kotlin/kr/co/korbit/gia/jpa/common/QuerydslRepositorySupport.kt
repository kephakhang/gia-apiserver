package kr.co.korbit.gia.jpa.common

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Sort
import org.springframework.data.querydsl.QSort
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
abstract class QuerydslRepositorySupport<T>() {

    lateinit var queryFactory: JPAQueryFactory

    //ref : https://stackoverflow.com/questions/13072378/how-can-i-convert-a-spring-data-sort-to-a-querydsl-orderspecifier
    open fun ordable(sort: Sort, builder: PathBuilder<*>): MutableList<OrderSpecifier<*>> {

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

    open fun select(): JPAQuery<T> {
        //ToDo : Unchecked cast : JPAQuery<*>! to JPAQuery<T> ??? fix it
        return queryFactory.query() as JPAQuery<T>
    }

//    open fun<T> applyPagination(pageable: Pageable, contentQuery: Function<JPAQueryFactory, JPAQuery> ): Page<T> {
//        val jpaQuery = contentQuery.apply(queryFactory)
//        val content: List<T> = getQuerydsl().applyPagination(pageable,jpaQuery).fetch();
}