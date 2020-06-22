package kr.co.korbit.gia.jpa.common

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.querydsl.QSort
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
abstract class BaseRepository(query: JPAQueryFactory) : QuerydslRepositorySupport(BaseRepository::class.java) {

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
}