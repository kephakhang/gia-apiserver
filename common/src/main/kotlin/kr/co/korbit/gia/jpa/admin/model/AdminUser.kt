package kr.co.korbit.gia.jpa.admin.model

import com.fasterxml.jackson.annotation.JsonInclude
import kr.co.korbit.gia.jpa.common.BaseModifyerPersistable
import kr.co.korbit.gia.jpa.common.BasePersistable
import kr.co.korbit.gia.jpa.common.UserStatus
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.Type
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*
@Entity(name = "AdminUser")
@Table(name = "admin.admin_users")
@Cacheable
@JsonInclude(JsonInclude.Include.NON_NULL)
class AdminUser (

    var email: String,

    var password: String,

    var phone: String,

    var roles: String,

    var name: String? = null,

    var nick: String? = null,

    var description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: UserStatus

) : BaseModifyerPersistable(){
}

