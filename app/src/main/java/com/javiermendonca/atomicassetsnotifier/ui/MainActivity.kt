package com.javiermendonca.atomicassetsnotifier.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.javiermendonca.atomicassetsnotifier.R
import com.javiermendonca.atomicassetsnotifier.data.api.RetrofitBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: AtomicNftsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(
            this,
            AtomicNftsViewModelFactory(RetrofitBuilder.chainApi, application)
        ).get(AtomicNftsViewModel::class.java)
        viewModel.fetchAtomicDrops()
    }
}