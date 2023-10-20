package com.example.cashapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.editTextTextEmailAddress)
        passwordEditText = findViewById(R.id.editTextTextPassword)
        loginButton = findViewById(R.id.loginbtn)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (isValidEmail(email) && isValidPassword(password)) {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            } else {
                if (!isValidEmail(email)) {
                    Toast.makeText(
                        this,
                        "Invalid email format. Please use your iacademy.edu.ph email.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Invalid password. Password should be at least 8 characters long " +
                                "and contain at least 1 number and 1 uppercase letter.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            //connect to welcome activity
            if(isValidEmail(email) && isValidPassword(password)){
                if (validateLogin(binding.email.text.toString(), binding.password.text.toString())){
                    val intent = Intent(this@LoginActivity,WelcomeActivity::class.java)
                    intent.putExtra("email", binding.emaiL.text.toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Invalid login credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    //validate functions for welcome activity
    private fun validateLogin(email : String, password : String) : Boolean {
        //send email and password to API and let API validate the credentials
        //check API response
        return true
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$".toRegex()
        return password.matches(passwordPattern)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        return email.matches(emailPattern)
    }
}