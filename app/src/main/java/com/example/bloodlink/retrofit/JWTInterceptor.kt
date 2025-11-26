package com.example.bloodlink.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class JWTInterceptor(private val tokenProvider: () -> String?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // URLs that should not carry Authorization
        val excludedPaths = listOf(
            "api/v1/auth/logIn",
            "api/v1/auth/signUp",
            "api/v1/auth/authenticate",
            "api/v1/auth/signUp",
            "api/v1/auth/logout"
        )

        // If this request matches an excluded path → skip adding token
        if (excludedPaths.any { request.url.encodedPath.endsWith(it) }) {
            return chain.proceed(request)
        }

        val token = tokenProvider()

        return if (token.isNullOrEmpty()) {
            chain.proceed(request)
        } else {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            chain.proceed(newRequest)
        }
    }

}