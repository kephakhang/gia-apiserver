package kr.co.korbit.gia.exception

import kr.co.korbit.common.error.ErrorCode
import org.springframework.http.HttpStatus

open class KorbitException(val code: ErrorCode, cause: Throwable?, vararg args: Any?) : Exception(cause){
    val httpStatus: HttpStatus = HttpStatus.OK
    private val serialVersionUID = 1756502187423843041L

    val argList = ArrayList<Any>()

    init {
        for (x: Int in 1 .. args.size) argList.add(args.get(x) as Any)
    }

}

class SessionNotFoundException( cause: Throwable?, vararg args: Any?): KorbitException(ErrorCode.E00003, cause, args) {
}