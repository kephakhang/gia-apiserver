package kr.co.korbit.gia.jpa.korbit.model.dto

import kr.co.korbit.gia.jpa.common.UserStatus
import kr.co.korbit.gia.jpa.test.model.User
import org.hibernate.annotations.Type
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated

class UserDto(
    var uuid: String,
    var email: String?,
    var password: String,
    var signInCount: Int,
    var name: String? = null,
    var nick: String? = null,
    var phone: String? = null,
    var permit: Int? = null,
    var gender: String,
    var nationality: String,
    var countryCode: String,
    var status: UserStatus,
    var signUpPlatformId: Long? = null,
    var isIdentifiedForCoins: Boolean = false,
    var isIdentifiedForFiats: Boolean = false,
    var isCorporation: Boolean = false
) {
    val permissionList: List<String>
        get() {
            return arrayListOf("/public", "/ineternal", "/admin")
        }

    val roleList: List<String>
        get() {
            return arrayListOf("USER", "AGENT", "ADMIN")
        }

    val role: String
        get() {
            return "USER"
        }
}