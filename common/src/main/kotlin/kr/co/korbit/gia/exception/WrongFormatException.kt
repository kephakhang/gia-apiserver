package kr.co.korbit.gia.exception

import org.springframework.http.HttpStatus

class WrongFormatException(vararg args: Any?) : InternalException(*args){
}