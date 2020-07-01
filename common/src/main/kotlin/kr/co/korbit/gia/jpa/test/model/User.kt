package kr.co.korbit.gia.jpa.test.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ser.Serializers
import kr.co.korbit.gia.jpa.common.BasePersistable
import kr.co.korbit.gia.jpa.common.UserStatus
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.Type
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*
@Entity(name = "User")
@Table(name = "test.users")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
class User (

    @Column(name = "uuid")
    var uuid: String,

    @Column(name = "email")
    var email: String? = null,

    @Column(name = "user_hash")
    var userHash: String? = null,

    @Column(name = "encrypted_password")
    var encryptedPassword: String,

    @Column(name = "encrypted_password_set_at")
    var encryptedPasswordSetAt: LocalDateTime? = null,

    @Column(name = "reset_password_token")
    var resetPasswordToken: String? = null,

    @Column(name = "reset_password_sent_at")
    var resetPasswordSentAt: LocalDateTime? = null,

    @Column(name = "remember_created_at")
    var rememberCreatedAt: LocalDateTime? = null,

    @Column(name = "sign_in_count")
    var signInCount: Int,

    @Column(name = "current_sign_in_at")
    var currentSignInAt: LocalDateTime? = null,

    @Column(name = "last_sign_in_at")
    var lastSignInAt: LocalDateTime? = null,

    @Column(name = "current_sign_in_ip")
    var currentSignInIp: String? = null,

    @Column(name = "last_sign_in_ip")
    var lastSignInIp: String? = null,

    @Column(name = "confirmation_token")
    var confirmationToken: String? = null,

    @Column(name = "confirmed_at")
    var confirmedAt: LocalDateTime? = null,

    @Column(name = "confirmation_sent_at")
    var confirmationSentAt: LocalDateTime? = null,

    @Column(name = "unconfirmed_email")
    var unconfirmedEmail: String? = null,

    @Column(name = "failed_attempts")
    var failedAttempts: Int = 0,

    @Column(name = "unlock_token")
    var unlockToken: String? = null,

    @Column(name = "unlock_sent_at")
    var unlockSentAt: LocalDateTime? = null,

    @Column(name = "locked_at")
    var lockedAt: LocalDateTime? = null,

    @Column(name = "authentication_token")
    var authenticationToken: String? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "nick")
    var nick: String? = null,

    @Column(name = "phone")
    var phone: String? = null,

    @Column(name = "permit")
    var permit: Int? = null,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,

    @Column(name = "dob")
    var dob: LocalDate? = null,

    @Column(name = "gender")
    var gender: String,

    @Column(name = "nationality")
    var nationality: String,

    @Column(name = "country_code")
    var countryCode: String,

    @Column(name = "namechecked_at")
    var namecheckedAt: LocalDateTime? = null,

    @Type(type = "yes_no")
    @Column(name = "terms")
    var terms: Boolean = false,

    @Column(name = "entity")
    var entity: String? = null,

    @Column(name = "registration_number")
    var registrationNumber: String? = null,

    @Column(name = "change_confirmation_token")
    var changeConfirmationToken: String? = null,

    @Column(name = "change_confirmed_at")
    var changeConfirmedAt: LocalDateTime? = null,

    @Column(name = "change_confirmation_sent_at")
    var changeConfirmationSentAt: LocalDateTime? = null,

    @Column(name = "namecheck_status")
    var namecheckStatus: String? = null,

    @Column(name = "namecheck_status_code")
    var namecheckStatusCode: String? = null,

    @Column(name = "namechecked_by")
    var namecheckedBy: Long? = null,

    @Column(name = "captcha_at")
    var captchaAt: LocalDateTime? = null,

    @Column(name = "no_captcha_until")
    var noCaptchaUntil: LocalDateTime? = null,

    @Column(name = "join_reason")
    var joinReason: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: UserStatus,

    @Column(name = "sign_up_platform_id")
    var signUpPlatformId: Long? = null,

    @Column(name = "is_identified_for_coins")
    @Type(type = "yes_no")
    var isIdentifiedForCoins: Boolean = false,

    @Column(name = "is_identified_for_fiats")
    @Type(type = "yes_no")
    var isIdentifiedForFiats: Boolean = false,

    @Column(name = "is_corporation")
    @Type(type = "yes_no")
    var isCorporation: Boolean = false
) : BasePersistable(){
}

