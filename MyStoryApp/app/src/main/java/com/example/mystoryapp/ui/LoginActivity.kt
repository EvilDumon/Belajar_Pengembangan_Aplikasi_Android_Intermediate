package com.example.mystoryapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.data.LoginPreferences
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.ui.viewmodel.LoginViewModel
import com.example.mystoryapp.ui.viewmodel.LoginViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupView()

        val pref = LoginPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this, LoginViewModelFactory(pref))[LoginViewModel::class.java]

        loginViewModel.getTokenLogin().observe(this) {
            if (it != "") {
                MainActivity.token = it
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        binding.btnLogin.setOnClickListener{
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (isEmailValid(email) && password.length >= 8) {
                loginViewModel.postLogin(email, password)
                loginViewModel.isFailed.observe(this){
                    if (it == false) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                        startActivity(intent)
                    }
                }
            }
        }

        binding.tvSignUp.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvLoginTitle, View.ALPHA, 1F).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1F).setDuration(500)
        val text = ObjectAnimator.ofFloat(binding.tvTxt, View.ALPHA, 1F).setDuration(500)
        val textSignUp = ObjectAnimator.ofFloat(binding.tvSignUp, View.ALPHA, 1F).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(text, textSignUp)
        }
        AnimatorSet().apply {
            playSequentially(
                title,
                edtEmail,
                edtPassword,
                btnLogin,
                together
            )
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}