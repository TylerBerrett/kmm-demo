package com.tylerb.kmm.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tylerb.kmm.android.databinding.ActivityMainBinding
import com.tylerb.kmm.shared.SpaceXSDK
import com.tylerb.kmm.shared.cache.DatabaseDriverFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val mainScope = MainScope()

    private lateinit var binding: ActivityMainBinding

    private val launchesRvAdapter = LaunchesRvAdapter(listOf())

    private val sdk = SpaceXSDK(DatabaseDriverFactory(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "SpaceX Launches"

        with(binding.launchesListRv) {
            adapter = launchesRvAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }


        binding.swipeContainer.setOnRefreshListener {
            binding.swipeContainer.isRefreshing = false
            displayLaunches(true)
        }

        displayLaunches(false)

    }

    private fun displayLaunches(needReload: Boolean) {
        binding.progressBar.isVisible = true

        mainScope.launch {
            kotlin.runCatching {
                sdk.getLaunches(needReload)
            }.onSuccess {
                launchesRvAdapter.launches = it
                launchesRvAdapter.notifyDataSetChanged()
            }.onFailure {
                Log.e("ERROR", it.toString(), it)
                Snackbar.make(binding.root, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
            }
            binding.progressBar.isVisible = false
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

}
