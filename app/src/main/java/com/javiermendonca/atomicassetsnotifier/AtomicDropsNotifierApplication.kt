package com.javiermendonca.atomicassetsnotifier

import android.app.Application
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.javiermendonca.atomicassetsnotifier.data.api.RetrofitBuilder
import com.javiermendonca.atomicassetsnotifier.data.repository.AtomicDropRepository
import com.javiermendonca.atomicassetsnotifier.data.worker.AtomicDropWorkerFactory

class AtomicDropsNotifierApplication : Application(), Configuration.Provider {

    override fun getWorkManagerConfiguration(): Configuration {
        val workingFactory = DelegatingWorkerFactory().apply {
            addFactory(
                AtomicDropWorkerFactory(
                    AtomicDropRepository(
                        RetrofitBuilder.chainApi,
                        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                    )
                )
            )
        }

        return Configuration.Builder()
            .setWorkerFactory(workingFactory)
            .apply {
                if (BuildConfig.DEBUG) {
                    setMinimumLoggingLevel(android.util.Log.DEBUG)
                } else {
                    setMinimumLoggingLevel(android.util.Log.ERROR)
                }
            }
            .build()
    }

    companion object {
        const val SHARED_PREFS = "AtomicPreferences"
    }

}