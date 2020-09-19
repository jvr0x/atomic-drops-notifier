package com.javiermendonca.atomicdropsnotifier.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.javiermendonca.atomicdropsnotifier.data.network.exceptions.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityStatusInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw NoConnectionException()
        }

        return chain.proceed(chain.request())
    }

    private fun isConnected(): Boolean =
        (context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager).run {
            return getNetworkCapabilities(activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                ?: false
        }
}