package kr.co.korbit.gia.jpa.test.model

import com.fasterxml.jackson.annotation.JsonInclude
import kr.co.korbit.gia.jpa.common.AbstractJpaPersistable
import kr.co.korbit.gia.jpa.common.UserStatus
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.Type
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "User")
@Table(name = "test.user")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Users (

    @Column(name = "uuid")
    var uuid: String,

    @Column(name = "email")
    var email: String? = null,

    @Column(name = "user_hash")
    var user_hash: String? = null,

    @Column(name = "encrypted_password")
    var encrypted_password: String,

    @Column(name = "encrypted_password_set_at")
    var encrypted_password_set_at: LocalDateTime? = null,

    @Column(name = "reset_password_token")
    var reset_password_token: String? = null,

    @Column(name = "reset_password_sent_at")
    var reset_password_sent_at: LocalDateTime? = null,

    @Column(name = "sign_in_count")
    var sign_in_count: Int,

    @Column(name = "current_sign_in_at")
    var current_sign_in_at: LocalDateTime? = null,

    @Column(name = "last_sign_in_at")
    var last_sign_in_at: LocalDateTime? = null,

    @Column(name = "current_sign_in_ip")
    var current_sign_in_ip: String? = null,

    @Column(name = "last_sign_in_ip")
    var last_sign_in_ip: String? = null,

    @Column(name = "confirmation_token")
    var confirmation_token: String? = null,

    @Column(name = "confirmed_at")
    var confirmed_at: LocalDateTime? = null,

    @Column(name = "confirmation_sent_at")
    var confirmation_sent_at: LocalDateTime? = null,

    @Column(name = "unconfirmed_email")
    var unconfirmed_email: String? = null,

    @Column(name = "failed_attempts")
    var failed_attempts: Int = 0,

    @Column(name = "unlock_token")
    var unlock_token: String? = null,

    @Column(name = "unlock_sent_at")
    var unlock_sent_at: LocalDateTime? = null,

    @Column(name = "locked_at")
    var locked_at: LocalDateTime? = null,

    @Column(name = "authentication_token")
    var authentication_token: String? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "nick")
    var nick: String? = null,

    @Column(name = "phone")
    var phone: String? = null,

    @Column(name = "permit")
    var permit: Int? = null,

    @Column(name = "deleted_at")
    var deleted_at: LocalDateTime? = null,

    @Column(name = "dob")
    var dob: LocalDate? = null,

    @Column(name = "gender")
    var gender: String,

    @Column(name = "nationality")
    var nationality: String,

    @Column(name = "country_code")
    var country_code: String,

    @Column(name = "namechecked_at")
    var namechecked_at: LocalDateTime? = null,

    @Type(type = "yes_no")
    @Column(name = "terms")
    var terms: Boolean = false,

    @Column(name = "entity")
    var entity: String? = null,

    @Column(name = "registration_number")
    var registration_number: String? = null,

    @Column(name = "change_confirmation_token")
    var change_confirmation_token: String? = null,

    @Column(name = "change_confirmed_at")
    var change_confirmed_at: LocalDateTime? = null,

    @Column(name = "change_confirmation_sent_at")
    var change_confirmation_sent_at: LocalDateTime? = null,

    @Column(name = "namecheck_status")
    var namecheck_status: String? = null,

    @Column(name = "namecheck_status_code")
    var namecheck_status_code: String? = null,

    @Column(name = "namechecked_by")
    var namechecked_by: Long? = null,

    @Column(name = "captcha_at")
    var captcha_at: LocalDateTime? = null,

    @Column(name = "no_captcha_until")
    var no_captcha_until: LocalDateTime? = null,

    @Column(name = "join_reason")
    var join_reason: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: UserStatus,

    @Column(name = "sign_up_platform_id")
    var sign_up_platform_id: Long? = null,

    @Column(name = "is_identified_for_coins")
    @Type(type = "yes_no")
    var is_identified_for_coins: Boolean = false,

    @Column(name = "is_identified_for_fiats")
    @Type(type = "yes_no")
    var is_identified_for_fiats: Boolean = false,

    @Column(name = "is_corporation")
    @Type(type = "yes_no")
    var is_corporation: Boolean = false
) : AbstractJpaPersistable(){
}

typealias Session = Users
typealias Agent = Users
typealias Admin = Users