package com.javiermendonca.atomicdropsnotifier.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.javiermendonca.atomicdropsnotifier.R
import com.javiermendonca.atomicdropsnotifier.data.api.RetrofitBuilder
import com.javiermendonca.atomicdropsnotifier.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: AtomicDropsViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(
            this,
            AtomicDropsViewModelFactory(
                RetrofitBuilder.chainApi,
                RetrofitBuilder.atomicAssetsApi,
                application
            )
        ).get(AtomicDropsViewModel::class.java)
        viewModel.fetchAtomicDrops()

        val adapter = AtomicDropsAdapter()
        binding.atomicDrops.layoutManager = GridLayoutManager(this, 1)
        binding.atomicDrops.adapter = adapter
        viewModel.atomicDrops.observe(this, { atomicDrops -> adapter.setAtomicDrops(atomicDrops) })
    }
}