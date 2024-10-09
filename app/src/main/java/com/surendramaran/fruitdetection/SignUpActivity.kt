package com.surendramaran.fruitdetection

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.surendramaran.fruitdetection.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignupBinding
    private val db = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.buttonSignup.setOnClickListener {
            val name = binding.etName.text.toString()
            val phone = binding.etPhone.text.toString()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            val confPass = binding.etConfirmPassword.text.toString()
            if (checkAllFields(name, phone, email, pass, confPass)) {
                createUser(name, phone, email, pass)
            }
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainHomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val signInButton = findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val userInfo = mapOf(
                    "name" to (user?.displayName ?: ""),
                    "phone" to "", // Google sign-in doesn't provide phone number
                    "email" to (user?.email ?: "")
                )
                user?.let {
                    db.child("users").child(it.uid)
                        .setValue(userInfo)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Signed in as ${user.displayName}", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainHomeActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { exception ->
                            Log.e("RealtimeDatabase", "Error adding user: $exception")
                            Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun checkAllFields(name: String, phone: String, email: String, pass: String, confPass: String): Boolean {
        if (name.isEmpty()) {
            binding.textInputLayoutName.error = "This field is required"
            return false
        }
        if (phone.isEmpty()) {
            binding.textInputLayoutPhone.error = "This field is required"
            return false
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            binding.textInputLayoutPhone.error = "Invalid phone number"
            return false
        }
        if (email.isEmpty()) {
            binding.textInputLayoutEmail.error = "This field is required"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.error = "Invalid email format"
            return false
        }
        if (pass.isEmpty()) {
            binding.textInputLayoutPassword.error = "This field is required"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        if (pass.length < 6) {
            binding.textInputLayoutPassword.error = "Password must be at least 6 characters"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        if (confPass.isEmpty()) {
            binding.textInputLayoutConfirmPassword.error = "This field is required"
            binding.textInputLayoutConfirmPassword.errorIconDrawable = null
            return false
        }
        if (pass != confPass) {
            binding.textInputLayoutPassword.error = "Passwords do not match"
            return false
        }
        return true
    }

    private fun createUser(name: String, phone: String, email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                    if (verificationTask.isSuccessful) {
                        val userInfo = mapOf(
                            "name" to name,
                            "phone" to phone,
                            "email" to email
                        )
                        user.let {
                            db.child("users").child(it.uid)
                                .setValue(userInfo)
                                .addOnSuccessListener {
                                    auth.signOut()
                                    Toast.makeText(this, "Account created. Please verify your email.", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, SigninActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("RealtimeDatabase", "Error adding user: $exception")
                                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Log.e("Error", task.exception.toString())
                Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
