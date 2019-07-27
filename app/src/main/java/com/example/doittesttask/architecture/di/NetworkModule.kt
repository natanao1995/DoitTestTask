package com.example.doittesttask.architecture.di

import com.example.doittesttask.Constants.BASE_URL
import com.example.doittesttask.data.User
import com.example.doittesttask.data.remote.DoitService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    val networkModule = module {
        single { createOkHttpClient(get()) }
        single { createRetrofitInstance(get()) }
        single { createTMDbService(get()) }
    }

    private fun createOkHttpClient(user: User): OkHttpClient {
        val authInterceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val rawRequest = chain.request()

                if (rawRequest.method().equals("post", true) &&
                    (rawRequest.url().encodedPath().equals("/users", true) ||
                            rawRequest.url().encodedPath().equals("/auth", true))) {
                    return chain.proceed(rawRequest)
                }

                val request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer ${user.token}")
                    .build()

                return chain.proceed(request)
            }
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private fun createRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private fun createTMDbService(retrofit: Retrofit): DoitService {
        return retrofit.create(DoitService::class.java)
    }
}