package kr.co.korbit.gia.exception

import org.springframework.http.HttpStatus

class IllegalStateException(vararg args: Any?) : InternalException(*args){
}