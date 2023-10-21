package com.example.cashapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cashapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignin.setOnClickListener {
            var errorCount = 0

            if (binding.tieEmail.text.isNullOrBlank())
            {
                errorCount++
                binding.tieEmail.error = "This field is required"

            }

            if (binding.tiePassword.text.isNullOrBlank())
            {
                errorCount++
                binding.tiePassword.error = "This field is required"

            }

            if (binding.tiePassword.text!!.length < 8)
            {
                errorCount++
                binding.tiePassword.error = "Password must contain at least 8 characters."

            }

            if (checkString(binding.tiePassword.text.toString()) == false)
            {
                errorCount++
                binding.tiePassword.error = "Password must contain at least 1 number and uppercase letter."
            }

            if(errorCount <= 0){
                if (validateLogin(binding.tieEmail.text.toString(), binding.tiePassword.text.toString())){
                    val intent = Intent(this@LoginActivity,WelcomeActivity::class.java)
                    intent.putExtra("email", binding.tieEmail.text.toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    //finish()

                } else {
                    Toast.makeText(this, "Invalid login credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun validateLogin (email : String, password : String) : Boolean {
        return true
    }

    private fun checkString(str: String): Boolean {
        var ch: Char
        var upperCaseFlag = false
        var numberFlag = false
        for (i in 0 until str.length) {
            ch = str[i]
            if (Character.isDigit(ch)) {
                numberFlag = true
            } else if (Character.isUpperCase(ch)) {
                upperCaseFlag = true
            }

            if (numberFlag && upperCaseFlag) return true
        }
        return false
    }
}