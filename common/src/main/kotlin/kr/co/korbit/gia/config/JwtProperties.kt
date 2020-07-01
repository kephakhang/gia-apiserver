package kr.co.korbit.gia.config

class JwtProperties() {
    companion object {
        val SECRET = "kobit-gia-apiserver"
        val EXPIRATION_TIME = 864000000
        val TOKEN_PREFIX = "Bearer"
        val HEADER_STRING = "Authorization"
    }
}
