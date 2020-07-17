


object JwtSecurityConstants {
    val AUTH_LOGIN_URL = "/admin/v1/authenticate"

    // Signing key for HS512 algorithm
    // You can use the page http://www.allkeysgenerator.com/ to generate all kinds of keys
    val JWT_SECRET = "8b4da347379e74a4a0eacc13cd1faa773bc414e6eb64304fdc8caa3d11a0ced8"

    // JWT token defaults
    val TOKEN_HEADER = "Authorization"
    val TOKEN_PREFIX = "Bearer "
    val TOKEN_TYPE = "JWT"
    val TOKEN_ISSUER = "secure-api"
    val TOKEN_AUDIENCE = "secure-app"
    val EXPIRATION_TIME = 864000000

}

enum class Role {
    ADMIN,
    AGENT,
    MEMBER,
    PUBLIC,
}
