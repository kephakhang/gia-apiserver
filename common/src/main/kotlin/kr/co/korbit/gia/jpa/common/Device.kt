package kr.co.korbit.gia.jpa.common

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class Device(
    val last_access_date: LocalDateTime? = null,
    val platform: String? = null,
    val properties: Map<String, Any>? = null
) : BasePersistable() {
    companion object {
        const val serialVersionUID = 4357098649071892749L
    }
}