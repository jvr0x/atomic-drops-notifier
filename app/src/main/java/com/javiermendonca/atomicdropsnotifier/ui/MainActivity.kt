package com.javiermendonca.atomicdropsnotifier.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.javiermendonca.atomicdropsnotifier.BuildConfig
import com.javiermendonca.atomicdropsnotifier.R
import com.javiermendonca.atomicdropsnotifier.data.api.RetrofitBuilder
import com.javiermendonca.atomicdropsnotifier.databinding.AboutDialogBinding
import com.javiermendonca.atomicdropsnotifier.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: AtomicDropsViewModel
    private lateinit var binding: ActivityMainBinding
    private val remoteConfig by lazy { FirebaseRemoteConfig.getInstance() }

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

        setUpFirebaseRemoteConfig()
    }

    private fun setUpFirebaseRemoteConfig() {
        val configSettings = FirebaseRemoteConfigSettings.Builder().run {
            minimumFetchIntervalInSeconds = REMOTE_CONFIG_FETCH_INTERVAL
            build()
        }

        remoteConfig.apply {
            setDefaultsAsync(R.xml.remote_config_defaults)
            setConfigSettingsAsync(configSettings)
            fetchAndActivate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.about_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_show_about -> {
                val aboutDialogBinding = DataBindingUtil.inflate<AboutDialogBinding>(
                    LayoutInflater.from(this),
                    R.layout.about_dialog,
                    null,
                    false
                ).apply {
                    appName = getString(R.string.app_name)
                    donationAccount = remoteConfig.getString(getString(R.string.developer_account))
                    appVersion = BuildConfig.VERSION_NAME
                    developerTwitter.setOnClickListener { openUrl(getString(R.string.about_twitter_link)) }
                    appGithub.setOnClickListener { openUrl(getString(R.string.about_github_link)) }
                }

                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.about))
                    .setView(aboutDialogBinding.root)
                    .setCancelable(true)
                    .setNeutralButton(android.R.string.ok) { _, _ -> }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openUrl(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

    companion object {
        const val REMOTE_CONFIG_FETCH_INTERVAL = 84600L
    }
}