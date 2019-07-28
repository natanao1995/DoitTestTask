package com.example.doittesttask.architecture.di

import com.example.doittesttask.Constants.BASE_URL
import com.example.doittesttask.architecture.interceptor.AuthInterceptor
import com.example.doittesttask.architecture.interceptor.NetworkConnectionInterceptor
import com.example.doittesttask.data.remote.DoitService
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

object NetworkModule {
    val networkModule = module {
        single { createOkHttpClient(get(), get()) }
        single { createRetrofitInstance(get()) }
        single { createTMDbService(get()) }
        single { NetworkConnectionInterceptor(androidContext()) }
        single { AuthInterceptor(androidContext(), get()) }
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
            .addConverterFactory(EnumConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private fun createTMDbService(retrofit: Retrofit): DoitService {
        return retrofit.create(DoitService::class.java)
    }

    class EnumConverterFactory : Converter.Factory() {
        override fun stringConverter(
            type: Type?, annotations: Array<out Annotation>?,
            retrofit: Retrofit?
        ): Converter<*, String>? {
            if (type is Class<*> && type.isEnum) {
                return Converter<Any?, String> { value -> getSerializedNameValue(value as Enum<*>) }
            }
            return null
        }
    }

    fun <E : Enum<*>> getSerializedNameValue(e: E): String? {
        try {
            return e.javaClass.getField(e.name).getAnnotation(SerializedName::class.java).value
        } catch (exception: NoSuchFieldException) {
            exception.printStackTrace()
        }

        return null
    }
}