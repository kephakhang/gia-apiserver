package kr.co.korbit.gia.exception

import org.springframework.http.HttpStatus

class SessionNotFoundException (vararg args: Any?) : InternalException(*args){
}