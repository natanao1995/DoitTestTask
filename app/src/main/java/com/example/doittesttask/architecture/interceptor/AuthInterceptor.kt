package com.example.doittesttask.architecture.interceptor

import android.content.Context
import android.content.Intent
import com.example.doittesttask.data.User
import com.example.doittesttask.feature.signin.SigninActivity
import com.example.doittesttask.feature.signin.SigninActivity.Companion.EXTRA_KEY_UNAUTHORIZED
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context,
    private val user: User
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
            val intent = Intent()
            intent.putExtra(EXTRA_KEY_UNAUTHORIZED, true)
            intent.setClass(context, SigninActivity::class.java)
            intent.action = SigninActivity::class.java.name
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        return result
    }
}