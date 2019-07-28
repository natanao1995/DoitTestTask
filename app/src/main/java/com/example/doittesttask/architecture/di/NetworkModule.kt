package com.example.doittesttask.architecture.di

import com.example.doittesttask.Constants.BASE_URL
import com.example.doittesttask.architecture.interceptor.AuthInterceptor
import com.example.doittesttask.architecture.interceptor.NetworkConnectionInterceptor
import com.example.doittesttask.data.remote.DoitService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    val networkModule = module {
        single { createOkHttpClient(get(), get()) }
        single { createRetrofitInstance(get()) }
        single { createTMDbService(get()) }
        single { NetworkConnectionInterceptor(androidContext()) }
        single { AuthInterceptor(get(), get()) }
    }

    private fun createOkHttpClient(
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
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