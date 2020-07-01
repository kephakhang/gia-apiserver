package kr.co.korbit.gia.jpa.korbit.model.dto

import kr.co.korbit.gia.jpa.korbit.model.User

class SessionDto(
    val user: User
) {
}

typealias Session = SessionDto
typealias Agent = SessionDto
typealias Admin = SessionDto