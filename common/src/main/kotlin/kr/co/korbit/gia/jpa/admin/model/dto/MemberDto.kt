package kr.co.korbit.gia.jpa.admin.model.dto

import kr.co.korbit.gia.jpa.admin.model.AdminUser
import kr.co.korbit.gia.jpa.common.UserStatus

class MemberDto(
    val email: String,
    val phone: String,
    val roles: String,
    var password: String,
    var name: String? = null,
    var nick: String? = null,
    var description: String? = null
) {
    fun toAdminUser(): AdminUser? {
        val adminUser = AdminUser(
                this.email,
                this.phone,
                this.roles,
                this.password,
                this.name,
                this.nick,
                this.description,
                UserStatus.registered
            )

        return adminUser
    }
}