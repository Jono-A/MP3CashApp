package com.example.cashapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class AuthenticationViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val ref = Firebase.database.reference
    private var states = MutableLiveData<AuthenticationStates>()

    private lateinit var gso:GoogleSignInOptions

    fun getStates(): LiveData<AuthenticationStates> = states

    fun isSignedIn() {
        states.value = AuthenticationStates.IsSignedIn(auth.currentUser != null)
    }

    fun signUp(email : String, password : String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful) states.value = AuthenticationStates.SignedUp
            else states.value = AuthenticationStates.Error

        }.addOnFailureListener {
            states.value = AuthenticationStates.Error
        }
    }

    fun signIn(email : String, password : String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful) states.value = AuthenticationStates.SignedIn
            else states.value = AuthenticationStates.Error

        }.addOnFailureListener {
            states.value = AuthenticationStates.Error
        }
    }

    fun getUserProfile() {
        /*val user = auth.currentUser
        user?.let {
            var uid = it.uid
            var name = it.displayName
            var email = it.email
            var photoUrl = it.photoUrl
        }
        states.value = AuthenticationStates.Default(user)*/

        val objectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.getValue<Users>()
                states.value = AuthenticationStates.Default(users)
            }

            override fun onCancelled(error: DatabaseError) {
                states.value = AuthenticationStates.Error
            }

        }
        ref.child("code14demo/users/" + auth.currentUser?.uid).addValueEventListener(objectListener)
    }

    fun updateUserProfile(newName : String) {
        val userUpdates = userProfileChangeRequest {
            displayName = newName
        }

        auth.currentUser?.updateProfile(userUpdates)?.addOnCompleteListener {
            if(it.isSuccessful) states.value = AuthenticationStates.ProfileUpdated
            else states.value = AuthenticationStates.Error

        }?.addOnFailureListener {
            states.value = AuthenticationStates.Error
        }
    }

    private fun updateUserEmail(newEmail : String) {
        auth.currentUser?.updateEmail(newEmail)?.addOnCompleteListener {
            if(it.isSuccessful)states.value = AuthenticationStates.EmailUpdated
            else states.value = AuthenticationStates.Error

        }?.addOnFailureListener {
            states.value = AuthenticationStates.Error
        }
    }

    private fun updateUserPassword(newPassword : String) {
        auth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener {
            if(it.isSuccessful) states.value = AuthenticationStates.PasswordUpdated
            else states.value = AuthenticationStates.Error

        }?.addOnFailureListener {
            states.value = AuthenticationStates.Error
        }
    }

    fun sendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if(it.isSuccessful) states.value = AuthenticationStates.VerificationEmailSent
            else states.value = AuthenticationStates.Error

        }?.addOnFailureListener {
            states.value = AuthenticationStates.Error
        }
    }

    fun sendPasswordResetEmail(email : String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if(it.isSuccessful) states.value = AuthenticationStates.PasswordResetEmailSent
            else states.value = AuthenticationStates.Error

        }?.addOnFailureListener {
            states.value = AuthenticationStates.Error
        }
    }

    fun logOut() {
        auth.signOut()
        states.value = AuthenticationStates.LogOut
    }

    fun deleteUser() {
        auth.currentUser?.delete()?.addOnCompleteListener {
            if(it.isSuccessful) states.value = AuthenticationStates.UserDeleted
            else states.value = AuthenticationStates.Error

        }?.addOnFailureListener {
            states.value = AuthenticationStates.Error
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) states.value = AuthenticationStates.SignedIn
                else states.value = AuthenticationStates.Error
        }.addOnFailureListener {

        }
    }

    fun createUserRecord(name : String, program : String, section : String, email : String) {
        val users = Users(
            name,
            program,
            section,
            email
        )
        ref.child("code14demo/users/" + auth.currentUser?.uid).setValue(users).addOnCompleteListener {
            if(it.isSuccessful) states.value = AuthenticationStates.ProfileUpdated
            else states.value = AuthenticationStates.Error
        }
    }

}