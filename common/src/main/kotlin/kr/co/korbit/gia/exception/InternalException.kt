package kr.co.korbit.gia.exception

import org.springframework.http.HttpStatus

open class InternalException(vararg args: Any?) : Exception(){
    val httpStatus: HttpStatus = HttpStatus.OK
    private val serialVersionUID = 1756502187423843041L

    val argList = ArrayList<Any>()

    init {
        for (x: Int in 1 .. args.size) argList.add(args.get(x) as Any)
    }

}