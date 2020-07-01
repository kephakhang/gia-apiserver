package kr.co.korbit.gia.jpa.test.model

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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
class AdminUser (

    var email: String? = null,

    var password: String,

    var name: String,

    var nick: String?,

    var phone: String?,

    var description: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: UserStatus

) : BaseModifyerPersistable(){
}

