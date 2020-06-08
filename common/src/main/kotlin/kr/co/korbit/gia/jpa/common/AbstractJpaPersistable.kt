package kr.co.korbit.gia.jpa.common

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.util.ProxyUtils
import java.lang.reflect.Modifier
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.persistence.*




@MappedSuperclass
abstract class AbstractJpaPersistable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    var id: Long = 0L

    @CreatedDate
    @Column(name="created_at")
    var created_at: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))

    @LastModifiedDate
    @Column(name="updated_at")
    var updated_at: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))


    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as AbstractJpaPersistable

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
