package com.example.cashapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cashapp.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignupBinding
    private lateinit var viewModel : AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AuthenticationViewModel()
        viewModel.getStates().observe(this@SignupActivity) {
            handleState(it)
        }

        binding.btnSignup.setOnClickListener {
            viewModel.signUp(
                binding.tieEmail.text.toString(),
                binding.tiePassword.text.toString()
            )
        }

    }

    private fun handleState(state : AuthenticationStates) {
        when(state) {
            is AuthenticationStates.SignedUp -> viewModel.createUserRecord(
                binding.tieName.text.toString(),
                binding.tieEmail.text.toString(),
                binding.tiePassword.text.toString())

            is AuthenticationStates.ProfileUpdated -> {
                WelcomeActivity.launch(this@SignupActivity)
                finish()
            }
            else -> {}
        }
    }

    companion object {
        fun launch(activity: Activity) = activity.startActivity(Intent(activity, SignupActivity::class.java))
    }
}