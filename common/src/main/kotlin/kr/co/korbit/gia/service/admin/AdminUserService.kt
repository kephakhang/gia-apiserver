package kr.co.korbit.gia.service.admin

import kr.co.korbit.common.extensions.isEmail
import kr.co.korbit.common.extensions.isPhoneNumber
import kr.co.korbit.gia.jpa.admin.model.AdminUser
import kr.co.korbit.gia.jpa.admin.model.dto.MemberDto
import kr.co.korbit.gia.jpa.admin.repository.AdminUserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class AdminUserService(val adminUserRepository: AdminUserRepository) : UserDetailsService {

//    @Transactional
//    fun joinUser(memberDto: MemberDto) {
//        // 비밀번호 암호화
//        val passwordEncoder = BCryptPasswordEncoder()
//        memberDto.password = passwordEncoder.encode(memberDto.password)
//        val adminUser: AdminUser? = adminUserRepository.save(memberDto.toAdminUser())
//    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(userId: String): UserDetails? {

        val adminUserWrapper: Optional<AdminUser> = if( userId.isEmail() ) {
            adminUserRepository.findByEmail(userId)
        } else if( userId.isPhoneNumber() ) {
            adminUserRepository.findByPhone(userId)
        } else {
            Optional.ofNullable<AdminUser>(null)
        }

        return if( adminUserWrapper.isEmpty() ) {
            null
        } else {
            val adminUser: AdminUser = adminUserWrapper.get()
            val authorities: MutableList<GrantedAuthority> = ArrayList()
            val roles: List<String> = adminUser.roles.split(",")
            for(role:String in roles) {
                authorities.add(SimpleGrantedAuthority(role))
            }

            User(
                userId,
                adminUser.password,
                authorities
            )
        }
    }
}