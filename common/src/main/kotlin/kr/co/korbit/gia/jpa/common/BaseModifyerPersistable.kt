package kr.co.korbit.gia.jpa.common

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*



@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class BaseModifyerPersistable: BasePersistable() {


    @CreatedBy
    @Column(name="created_by", updatable = false)
    var createdBy: Long = 0L


    @LastModifiedBy
    @Column(name="updated_by")
    var updatedBy: Long = 0L

}

