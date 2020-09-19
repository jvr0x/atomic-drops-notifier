package com.javiermendonca.atomicdropsnotifier.data.api

import android.content.Context
import com.javiermendonca.atomicdropsnotifier.BuildConfig
import com.javiermendonca.atomicdropsnotifier.data.network.ConnectivityStatusInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private fun getRetrofit(baseUrl: String, context: Context): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(ConnectivityStatusInterceptor(context))
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

    fun chainApi(context: Context): ChainApi =
        getRetrofit(BuildConfig.WAX_BASE_URL, context).create(ChainApi::class.java)

    fun atomicAssetsApi(context: Context): AtomicAssetsApi =
        getRetrofit(BuildConfig.ATOMIC_BASE_URL, context).create(AtomicAssetsApi::class.java)
}