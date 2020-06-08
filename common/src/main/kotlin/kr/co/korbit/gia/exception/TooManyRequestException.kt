package kr.co.korbit.gia.exception

import org.springframework.http.HttpStatus

class TooManyRequestException(vararg args: Any?) : InternalException(*args){
}