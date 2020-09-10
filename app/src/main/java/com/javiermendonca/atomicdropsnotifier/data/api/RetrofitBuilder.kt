package com.javiermendonca.atomicdropsnotifier.data.api

import com.javiermendonca.atomicdropsnotifier.BuildConfig
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private fun getRetrofit(baseUrl: String): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
            }
            .build()

        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .build()
    }

    val chainApi: ChainApi = getRetrofit(BuildConfig.WAX_BASE_URL).create(ChainApi::class.java)
    val atomicAssetsApi: AtomicAssetsApi =
        getRetrofit(BuildConfig.ATOMIC_BASE_URL).create(AtomicAssetsApi::class.java)
}