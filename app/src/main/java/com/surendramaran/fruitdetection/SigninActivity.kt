package com.surendramaran.fruitdetection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.surendramaran.fruitdetection.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide action bar if present
        supportActionBar?.hide()

        auth = Firebase.auth

        binding.buttonSignin.setOnClickListener {
            val email = binding.signinEtEmail.text.toString()
            val pass = binding.signinEtpass.text.toString()
            if (checkAllFilled()) {
                signInUser(email, pass)
            }
        }

        binding.tvcreateAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.ForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgottenPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkAllFilled(): Boolean {
        val email = binding.signinEtEmail.text.toString()

        if (email.isEmpty()) {
            binding.signinTextInputLayoutEmail.error = "Email is required"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.signinTextInputLayoutEmail.error = "Invalid email format"
            return false
        }
        if (binding.signinEtpass.text.toString().isEmpty()) {
            binding.signinTextInputLayoutpass.error = "Password is required"
            binding.signinTextInputLayoutpass.errorIconDrawable = null
            return false
        }
        if (binding.signinEtpass.length() < 6) {
            binding.signinTextInputLayoutpass.error = "Password must be at least 6 characters"
            binding.signinTextInputLayoutpass.errorIconDrawable = null
            return false
        }
        return true
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { signInTask ->
            if (signInTask.isSuccessful) {
                val user = auth.currentUser
                if (user != null && user.isEmailVerified) {
                    Toast.makeText(this, "Successfully signed in", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainHomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Email not verified. Please verify your email to continue.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Log.e("SigninActivity", "Sign in failed", signInTask.exception)
                Toast.makeText(
                    this,
                    "Authentication failed. Please check your credentials and try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
