package kr.co.korbit.gia.jpa.admin.model.dto

import kr.co.korbit.common.extensions.isEmail
import kr.co.korbit.common.extensions.isPhoneNumber
import kr.co.korbit.gia.jpa.admin.model.AdminUser

class LoginDto(
    val memberId: String,
    var password: String
) {
//    fun toAdminUser(): AdminUser? {
//      return if( memberId.isEmail() ) {
//                val adminUser = AdminUser()
//                adminUser.email = this.memberId
//                adminUser.password = this.password
//                adminUser
//            }
//            else if( memberId.isPhoneNumber() ) {
//                val adminUser = AdminUser()
//                adminUser.phone = this.memberId
//                adminUser.password = this.password
//                adminUser
//            }
//            else {
//                null
//            }
//    }
}