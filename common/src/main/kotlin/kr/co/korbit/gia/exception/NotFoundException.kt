package kr.co.korbit.gia.exception

import org.springframework.http.HttpStatus

class NotFoundException(vararg args: Any?) : InternalException(*args){
}