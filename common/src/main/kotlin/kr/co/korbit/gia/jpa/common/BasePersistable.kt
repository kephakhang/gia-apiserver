package kr.co.korbit.gia.jpa.common

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import java.lang.reflect.Modifier
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.persistence.*



@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class BasePersistable: Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    var id: Long? = null

    @CreatedDate
    @Column(name="created_at", updatable = false)
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    @Column(name="updated_at")
    lateinit var updatedAt: LocalDateTime


    // annotation 으로 아래 기능 자동 수행
//    @PrePersist
//    fun prePersist() {
//        val now = LocalDateTime.now()
//        createdAt = now
//        updatedAt = now
//    }
//
//    @PreUpdate
//    fun preUpdate() {
//        val now = LocalDateTime.now()
//        updatedAt = now
//    }


    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as BasePersistable

        return (this.id == other.id)
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }

    override fun toString() = "Entity of type ${this.javaClass.name} with id: $id"

    //override fun toString() = this.reflectionToString(this)

    fun reflectionToString(obj: Any): String {
        val s = LinkedList<String>()
        var clazz: Class<in Any>? = obj.javaClass
        while (clazz != null) {
            for (prop in clazz.declaredFields.filterNot { Modifier.isStatic(it.modifiers) }) {
                prop.isAccessible = true
                s += "${prop.name}=" + prop.get(obj)?.toString()?.trim()
            }
            clazz = clazz.superclass
        }
        return "${obj.javaClass.simpleName}=[${s.joinToString(", ")}]"
    }

}

enum class UserStatus{
    registered, leaved, unconfirmed, dormant, failed, saved, deleted
}

enum class RefundStatus {
    denied, canceled, filled, requested
}
