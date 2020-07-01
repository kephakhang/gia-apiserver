package kr.co.korbit.gia.jpa.common

import org.springframework.data.domain.Sort

open class BaseService {

    open fun ordable(alias: String, sort: Sort): String {

        var orderStr: String = ""
        for (o in sort) {
            orderStr += if (o.isAscending) {
                alias + o.property + ",asc "
            } else {
                alias + o.property + ",desc "
            }
        }
        return orderStr
    }
}