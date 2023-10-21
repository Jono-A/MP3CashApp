package com.example.cashapp

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cashapp.databinding.ActivitySignupBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SigninActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySigninBinding
    private lateinit var viewModel : AuthenticationViewModel
    private lateinit var gso : GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AuthenticationViewModel()
        viewModel.getStates().observe(this@SigninActivity){
            renderUi(it)
        }

        createRequest()

        with(binding) {
            btnSignin.setOnClickListener {
                viewModel.signIn(
                    binding.tieEmail.text.toString(),
                    binding.tiePassword.text.toString()
                )
            }

            btnGoogle.setOnClickListener {
                mGoogleSignInClient.signOut()
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, 2)
            }

            tvSignup.apply {
                text = addClickableLink("Don't have an account yet? Sign up", "Sign up"){
                    SignupActivity.launch(this@SigninActivity)
                }
                movementMethod = LinkMovementMethod.getInstance()
            }
        }

    }

    private fun renderUi(states : AuthenticationStates) {
        when(states) {
            is AuthenticationStates.IsSignedIn -> {
                if(states.isSignedIn) {
                    WelcomeActivity.launch(this@SigninActivity)
                    finish()
                }
            }
            AuthenticationStates.SignedIn -> {
                WelcomeActivity.launch(this@SigninActivity)
                finish()
            }
            AuthenticationStates.Error -> {}
            else -> {}
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception

            try {
                val account = task.getResult(ApiException::class.java)!!
                viewModel.firebaseAuthWithGoogle(account)

            } catch (e: ApiException) {
                //TODO
            }

        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.isSignedIn()
    }

    private fun addClickableLink(fullText: String, linkText: String, callback: () -> Unit): SpannableString {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                callback.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.typeface = Typeface.DEFAULT_BOLD
            }

        }

        val startIndex = fullText.indexOf(linkText, 0, true)

        return SpannableString(fullText).apply {
            setSpan(clickableSpan, startIndex, startIndex + linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    companion object {
        fun launch(activity : Activity) = activity.startActivity(Intent(activity, SigninActivity::class.java))
    }
}