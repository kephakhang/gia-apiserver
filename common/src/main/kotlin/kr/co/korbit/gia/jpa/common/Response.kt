package kr.co.korbit.gia.jpa.common

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
open class Response(
    val success: Boolean,
    open val body:Any?,
    val requestId: String?,
    val requestUri: String?,
    val method: String?
) {

}

class Ok(override val body: Any?): Response(true, body, null, null, null) {

}