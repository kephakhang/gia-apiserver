package kr.co.korbit.gia.util

import kr.co.korbit.gia.jpa.test.model.User
interface PushResultHandler {
    fun error(arg: Any?, user: User?, cause: String?)
    fun send(arg: Any?, user: User?)
}
