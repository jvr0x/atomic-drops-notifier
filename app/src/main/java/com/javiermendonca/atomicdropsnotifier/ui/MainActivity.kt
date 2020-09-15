package com.javiermendonca.atomicdropsnotifier.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.about_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_show_about -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.about))
                    .setCancelable(true)
                    .setMessage(getString(R.string.about_message, getString(R.string.app_name)))
                    .setNeutralButton(android.R.string.ok) { _, _ -> }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}