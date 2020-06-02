package kr.co.korbit.gia.jpa.korbitapi.model

import com.fasterxml.jackson.annotation.JsonInclude
import kr.co.korbit.gia.jpa.entity.AbstractJpaPersistable
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.*

@Entity(name = "Clients")
@Table(name = "korbit_api.clients")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Clients (
    @Id
    @Column(name ="key")
    var key: String = "key",

    @Column(name ="encrypted_secret", nullable = false)
    var encrypted_secret: String = "encrypted_secret",

    @Column(name ="user_id", nullable = true)
    var user_id: Int? = null,

    @Column(name ="alias", nullable = false)
    var alias: String = "Default",

    @Column(name ="scope", nullable = false)
    var scope: String = "VIEW,TRADE,WITHDRAWAL",

    @Column(name ="is_renew_refresh_token", nullable = false)
    var is_renew_refresh_token: Boolean = true,

    @Column(name ="permission_code", nullable = true)
    var permission_code: Int? = null,

    @Column(name ="is_trusted_app", nullable = false)
    var is_trusted_app: Boolean = false,

    @Column(name ="memo", nullable = true)
    var memo: String? = null,

    @Column(name ="redirect_uri", nullable = true)
    var redirect_uri: String? = null,

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="created_at", nullable = false)
    var created_at: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="updated_at", nullable = false)
    var updated_at: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="deleted_at", nullable = true)
    var deleted_at: LocalDateTime? = null

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Clients
        if (key != other.key) return false
        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun toString() = "Entity of type ${this.javaClass.name} with id: $key"
}