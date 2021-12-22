package com.vandoc.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

private const val EXPIRED_TOKEN_IN_MILLIS = 28_800_000 // 8 Hours

fun generateJwt(username: String, email: String): String {
    val secret = System.getenv("JWT_SECRET")
    val issuer = System.getenv("JWT_ISSUER")
    val audience = System.getenv("JWT_AUDIENCE")

    return JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withIssuedAt(Date())
        .withClaim(
            "data", mapOf(
                "username" to username,
                "email" to email
            )
        )
        .withExpiresAt(Date(System.currentTimeMillis() + EXPIRED_TOKEN_IN_MILLIS))
        .sign(Algorithm.HMAC256(secret))
}