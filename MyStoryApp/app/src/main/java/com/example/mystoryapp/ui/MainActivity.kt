package com.example.mystoryapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.data.LoginPreferences
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.ui.recyclerview.LoadingStateAdapter
import com.example.mystoryapp.ui.recyclerview.RecyclerViewAdapter
import com.example.mystoryapp.ui.recyclerview.RecyclerViewData
import com.example.mystoryapp.ui.viewmodel.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var pref : LoginPreferences

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val logOutView = menu.findItem(R.id.logOut)
        logOutView.setOnMenuItemClickListener {
            loginViewModel.clearToken()
            val logOutIntent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(logOutIntent)
            true
        }
        val addStoryView = menu.findItem(R.id.addStory)
        addStoryView.setOnMenuItemClickListener {
            val addStoryIntent = Intent(this@MainActivity, PostActivity::class.java)
            startActivity(addStoryIntent)
            true
        }
        val mapsView = menu.findItem(R.id.maps)
        mapsView.setOnMenuItemClickListener {
            val mapsIntent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(mapsIntent)
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = LoginPreferences.getInstance(dataStore)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(pref))[LoginViewModel::class.java]

        loginViewModel.getTokenLogin().observe(this) {
            if (it == "") {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        showStories()
    }

    private fun showStories() {
        val adapter = RecyclerViewAdapter()
        val layoutManager = LinearLayoutManager(this)

        binding.rvListStories.let {
            it.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                })
            it.layoutManager = layoutManager
        }
        mainViewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter.setOnItemClickCallback(object : RecyclerViewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: RecyclerViewData) {
                val detailIntent = Intent(this@MainActivity, DetailStoryActivity::class.java)
                detailIntent.putExtra(DetailStoryActivity.DATA_ID, data.id)
                startActivity(detailIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        var token = "Token"
    }
}