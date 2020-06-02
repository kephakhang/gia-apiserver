package kr.co.korbit.gia.jpa.korbitapi.model

import com.fasterxml.jackson.annotation.JsonInclude
import kr.co.korbit.gia.jpa.entity.AbstractJpaPersistable
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.*

@Entity(name = "WhiteLists")
@Table(name = "korbit_api.white_lists")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
class WhiteLists (
    @Id
    @Column(name ="ip")
    var ip: String = "ip",

    @Column(name ="client_key", nullable = false)
    var client_key: String = "client_key"
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as WhiteLists
        if (ip != other.ip) return false
        return true
    }

    override fun hashCode(): Int {
        return ip.hashCode()
    }

    override fun toString() = "Entity of type ${this.javaClass.name} with id: $ip"
}