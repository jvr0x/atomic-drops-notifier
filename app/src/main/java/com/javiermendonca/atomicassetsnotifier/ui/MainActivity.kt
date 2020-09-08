package com.javiermendonca.atomicassetsnotifier.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.javiermendonca.atomicassetsnotifier.R
import com.javiermendonca.atomicassetsnotifier.data.api.RetrofitBuilder
import com.javiermendonca.atomicassetsnotifier.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: AtomicDropsViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(
            this,
            AtomicDropsViewModelFactory(RetrofitBuilder.chainApi, application)
        ).get(AtomicDropsViewModel::class.java)
        viewModel.fetchAtomicDrops()

        val adapter = AtomicDropsAdapter()
        binding.atomicDrops.layoutManager = GridLayoutManager(this, 2)
        binding.atomicDrops.adapter = adapter
        viewModel.atomicDrops.observe(this, { atomicDrops -> adapter.setAtomicDrops(atomicDrops) })
    }
}