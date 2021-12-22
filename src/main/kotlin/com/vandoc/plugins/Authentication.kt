package com.vandoc.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*


fun Application.configureAuthentication() {
    install(Authentication) {
        val secret = System.getenv("JWT_SECRET")
        val issuer = System.getenv("JWT_ISSUER")
        val audience = System.getenv("JWT_AUDIENCE")
        val jwtRealm = System.getenv("JWT_REALM")

        jwt {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )

            validate { credential ->
                val data = credential.payload.getClaim("data").asMap()
                if (data["username"] != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}
