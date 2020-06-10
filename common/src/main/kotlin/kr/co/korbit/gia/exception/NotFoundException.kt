package kr.co.korbit.gia.exception

class NotFoundException(cause: Throwable, vararg args: Any?) : InternalException(cause, *args){
}