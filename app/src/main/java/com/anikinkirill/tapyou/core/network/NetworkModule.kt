package com.anikinkirill.tapyou.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {

    singleOf(::TimeoutInterceptor)

    single {
        val httpBuilder = OkHttpClient.Builder()
        val timeoutInterceptor = get<TimeoutInterceptor>()

        httpBuilder
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(timeoutInterceptor)
            .build()
    }

    single {
        val okHttpClient = get<OkHttpClient>()
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
        }

        Retrofit.Builder()
            .baseUrl("https://hr-challenge.dev.tapyou.com/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(RemoteResourceCallAdapterFactory())
            .build()
    }
}