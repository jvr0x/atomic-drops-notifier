package com.javiermendonca.atomicassetsnotifier.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javiermendonca.atomicassetsnotifier.AtomicAssetsNotifierApplication
import com.javiermendonca.atomicassetsnotifier.data.api.ChainApi
import com.javiermendonca.atomicassetsnotifier.data.repository.AtomicNftRepository

class AtomicNftsViewModelFactory(
    private val chainApi: ChainApi,
    private val application: Application
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AtomicNftsViewModel::class.java)) {
            return AtomicNftsViewModel(
                AtomicNftRepository(
                    chainApi, application.getSharedPreferences(
                        AtomicAssetsNotifierApplication.SHARED_PREFS,
                        Application.MODE_PRIVATE
                    )
                ), application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}