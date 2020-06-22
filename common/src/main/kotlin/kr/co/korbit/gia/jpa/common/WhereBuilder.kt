package kr.co.korbit.gia.jpa.common

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.Visitor
import org.springframework.lang.Nullable

class WhereBuilder : Predicate, Cloneable {
    private var delegate: BooleanBuilder

    constructor() {
        delegate = BooleanBuilder()
    }

    constructor(predicate: Predicate) {
        delegate = BooleanBuilder(predicate)
    }

    fun and(right: Predicate): WhereBuilder {
        return WhereBuilder(delegate.and(right))
    }

    fun <V> optionalAnd(@Nullable param: V?, expression: Predicate): WhereBuilder {
       return if( param != null ) {
            this.and(expression)
        } else {
            this
        }
    }

    override fun <R : Any?, C : Any?> accept(v: Visitor<R, C>?, context: C?): R? {
        if (delegate != null) {
            return delegate.accept(v, context)
        } else {
            return null;
        }
    }

    override fun getType(): Class<out Boolean> {
        return Boolean::class.java
    }

    override fun not(): Predicate {
        if (delegate != null) {
            delegate = delegate.not()
        }
        return this
    }
}