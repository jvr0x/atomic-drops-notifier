package com.javiermendonca.atomicassetsnotifier.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javiermendonca.atomicassetsnotifier.AtomicDropsNotifierApplication
import com.javiermendonca.atomicassetsnotifier.data.api.ChainApi
import com.javiermendonca.atomicassetsnotifier.data.repository.AtomicDropRepository

class AtomicDropsViewModelFactory(
    private val chainApi: ChainApi,
    private val application: Application
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AtomicDropsViewModel::class.java)) {
            return AtomicDropsViewModel(
                AtomicDropRepository(
                    chainApi, application.getSharedPreferences(
                        AtomicDropsNotifierApplication.SHARED_PREFS,
                        Application.MODE_PRIVATE
                    )
                ), application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}