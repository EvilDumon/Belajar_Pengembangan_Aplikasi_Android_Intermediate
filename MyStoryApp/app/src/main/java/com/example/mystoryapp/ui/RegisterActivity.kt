package com.example.mystoryapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.ui.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupView()

        binding.btnRegister.setOnClickListener{
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (name.isNotEmpty()){
                if (isEmailValid(email)){
                    if ( password.length >=8){
                        registerViewModel.postRegister(name, email, password)
                        registerViewModel.isFailed.observe(this){
                            if (it == false){
                                finish()
                            }
                        }
                    }
                }
            }
        }

        registerViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1F).setDuration(500)
        val subtitle =
            ObjectAnimator.ofFloat(binding.tvRegisterSubtitle, View.ALPHA, 1F).setDuration(500)
        val edtName =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val edtEmail =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val edtPassword =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1F).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                subtitle,
                edtName,
                edtEmail,
                edtPassword,
                btnRegister
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