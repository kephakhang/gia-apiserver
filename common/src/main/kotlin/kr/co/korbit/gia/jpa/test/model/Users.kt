package kr.co.korbit.gia.jpa.test.model

import com.fasterxml.jackson.annotation.JsonInclude
import kr.co.korbit.gia.jpa.common.AbstractJpaPersistable
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.Type
import java.util.Date
import javax.persistence.*

//@Entity(name = "Users")
//@Table(name = "test.users")
//@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Users (

    @Column(name = "uuid")
    var uuid: String,

    @Column(name = "email")
    var email: String,

    @Column(name = "user_hash")
    var user_hash: String,

    @Column(name = "encrypted_password")
    var encrypted_password: String,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "encrypted_password_set_at")
    var encrypted_password_set_at: Date? = null,

    @Column(name = "reset_password_token")
    var reset_password_token: String,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reset_password_sent_at")
    var reset_password_sent_at: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "remember_created_at")
    var remember_created_at: Date? = null,

    @Column(name = "sign_in_count")
    var sign_in_count: Int,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "current_sign_in_at")
    var current_sign_in_at: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_sign_in_at")
    var last_sign_in_at: Date? = null,

    @Column(name = "current_sign_in_ip")
    var current_sign_in_ip: String,

    @Column(name = "last_sign_in_ip")
    var last_sign_in_ip: String,

    @Column(name = "confirmation_token")
    var confirmation_token: String,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "confirmed_at")
    var confirmed_at: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "confirmation_sent_at")
    var confirmation_sent_at: Date? = null,

    @Column(name = "unconfirmed_email")
    var unconfirmed_email: String,

    @Column(name = "failed_attempts")
    var failed_attempts: Int = 0,

    @Column(name = "unlock_token")
    var unlock_token: String = "",

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "unlock_sent_at")
    var unlock_sent_at: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "locked_at")
    var locked_at: Date? = null,

    @Column(name = "authentication_token")
    var authentication_token: String,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    var created_at: Date,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    var updated_at: Date,

    @Column(name = "name")
    var name: String,

    @Column(name = "nick")
    var nick: String,

    @Column(name = "phone")
    var phone: String,

    @Column(name = "permit")
    var permit: Int? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    var deleted_at: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dob")
    var dob: Date? = null,

    @Column(name = "gender")
    var gender: String,

    @Column(name = "nationality")
    var nationality: String,

    @Column(name = "country_code")
    var country_code: String,

    @Column(name = "namechecked_at")
    var namechecked_at: Date? = null,

    @Type(type = "yes_no")
    @Column(name = "terms")
    var terms: Boolean = false,

    @Column(name = "entity")
    var entity: String,

    @Column(name = "registration_number")
    var registration_number: String,

    @Column(name = "change_confirmation_token")
    var change_confirmation_token: String,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "change_confirmed_at")
    var change_confirmed_at: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "change_confirmation_sent_at")
    var change_confirmation_sent_at: Date? = null,

    @Column(name = "namecheck_status")
    var namecheck_status: String,

    @Column(name = "namecheck_status_code")
    var namecheck_status_code: String,

    @Column(name = "namechecked_by")
    var namechecked_by: Long,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "captcha_at")
    var captcha_at: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "no_captcha_until")
    var no_captcha_until: Date? = null,

    @Column(name = "join_reason")
    var join_reason: String,

    @Column(name = "status")
    var status: String,

    @Column(name = "sign_up_platform_id")
    var sign_up_platform_id: Long,

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
