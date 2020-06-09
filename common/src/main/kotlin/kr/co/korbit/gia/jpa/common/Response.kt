package kr.co.korbit.gia.jpa.common

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class Response(
    val success: Boolean,
    val body:Any,
    val requestId: String?,
    val requestUri: String?,
    val method: String?
) {

}