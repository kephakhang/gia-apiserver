package kr.co.korbit.gia.jpa.common

import com.fasterxml.jackson.annotation.JsonInclude
import kr.co.korbit.gia.jpa.test.model.User

@JsonInclude(JsonInclude.Include.NON_NULL)
class UserApp(
    var push_key: String,
    var device: Device? = null,
    var user: User? = null

)  {
}