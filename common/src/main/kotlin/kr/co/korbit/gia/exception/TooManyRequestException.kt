package kr.co.korbit.gia.exception

class TooManyRequestException(cause: Throwable, vararg args: Any?) : InternalException(cause, *args){
}