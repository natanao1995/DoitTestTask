package com.example.doittesttask.architecture.interceptor

import com.example.doittesttask.data.User
import com.example.doittesttask.feature.auth.AuthManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val user: User,
    private val authManager: AuthManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val rawRequest = chain.request()

        if (rawRequest.method().equals("post", true) &&
            (rawRequest.url().encodedPath().equals("/users", true) ||
                    rawRequest.url().encodedPath().equals("/auth", true))
        ) {
            return chain.proceed(rawRequest)
        }

        val token = user.token
        val result = if (token == null) {
            chain.proceed(rawRequest)
        } else {
            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer ${user.token}")
                .build()
            chain.proceed(request)
        }

        if (result.code() == 401 || result.code() == 500) { //server instead of sending invalid token sends me 500 Internal Server ¯\_(ツ)_/¯
            GlobalScope.launch {
                authManager.logout()
            }
        }

        return result
    }
}