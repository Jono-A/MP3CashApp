package com.example.cashapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cashapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            var errorCount = 0

            if (binding.email.text.isNullOrBlank())
            {
                errorCount++
                binding.email.error = "This field is required"

            }

            if (binding.password.text.isNullOrBlank())
            {
                errorCount++
                binding.password.error = "This field is required"

            }

            if (binding.password.text.length < 8)
            {
                errorCount++
                binding.password.error = "Password must contain at least 8 characters."

            }

            if (checkString(binding.password.text.toString()) == false)
            {
                errorCount++
                binding.password.error = "Password must contain at least 1 number and uppercase letter."
            }

            if(errorCount <= 0){
                if (validateLogin(binding.email.text.toString(), binding.password.text.toString())){
                    val intent = Intent(this@LoginActivity,WelcomeActivity::class.java)
                    intent.putExtra("email", binding.email.text.toString())
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