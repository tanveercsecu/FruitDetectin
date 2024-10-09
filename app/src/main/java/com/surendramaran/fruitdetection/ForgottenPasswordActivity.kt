package com.surendramaran.fruitdetection


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.google.firebase.auth.FirebaseAuth
import com.surendramaran.fruitdetection.databinding.ActivityForgottenPasswordBinding

class ForgottenPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityForgottenPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgottenPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.btnForgotten.setOnClickListener {
            val email = binding.forgotEtEmail.text.toString()
            if (ChekEmail()) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        // email is sent

                        Toast.makeText(this,"Emal sent",Toast.LENGTH_SHORT).show()
                        // destroy this activity
                        val intent = Intent(this,SigninActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
            }

            else {
                // Handle empty email input
            }
        }

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun ChekEmail():Boolean{
        val email=binding.forgotEtEmail.text.toString()


        if (binding.forgotEtEmail.text.toString()=="")
        {
            binding.forgotTextInputLayoutEmail.error="this is required fail"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.forgotTextInputLayoutEmail.error="check the email format"
            return false
        }
        return true
    }
}
