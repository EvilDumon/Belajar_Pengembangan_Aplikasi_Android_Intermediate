package com.example.mystoryapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mystoryapp.data.LoginPreferences
import com.example.mystoryapp.data.response.Story
import com.example.mystoryapp.databinding.ActivityDetailStoryBinding
import com.example.mystoryapp.ui.viewmodel.LoginViewModel
import com.example.mystoryapp.ui.viewmodel.LoginViewModelFactory
import com.example.mystoryapp.ui.viewmodel.StoryViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    private val storyViewModel by viewModels<StoryViewModel>()

    private lateinit var id: String
    private lateinit var photoUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = LoginPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this, LoginViewModelFactory(pref))[LoginViewModel::class.java]

        val id: String = intent.getStringExtra(DATA_ID).toString()

        loginViewModel.getTokenLogin().observe(this){token ->
            if (token == "") {
                val intent = Intent(this@DetailStoryActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }else {
                storyViewModel.onStory(id,"Bearer $token")
            }
        }

        storyViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        storyViewModel.detailStory.observe(this) {
            setDetailStory(it)
        }
    }

    private fun setDetailStory(story: Story?) {
        story?.let {
            binding.tvDetailName.text = it.name
            binding.tvDetailDescription.text = it.description

            id = it.id.toString()
            photoUrl = it.photoUrl.toString()
        }
        Glide.with(this)
            .load(photoUrl)
            .into(binding.ivDetailPhoto)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val DATA_ID = ""
    }
}