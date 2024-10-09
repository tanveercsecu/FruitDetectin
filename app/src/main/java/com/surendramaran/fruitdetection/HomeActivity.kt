package com.surendramaran.fruitdetection



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.surendramaran.fruitdetection.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        // Set up the sign-out button click listener
        binding.signout.setOnClickListener {
            auth.signOut()
            // Start SigninActivity
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.btnUpdatePassword.setOnClickListener {
            val user=auth.currentUser
            val password=binding.signinEtpass.text.toString()
            // Start SigninActivity

            if (checkpassword())
            {
                user?.updatePassword(password)?.addOnCompleteListener {
                    //if success full -- update success
                    if (it.isSuccessful)
                    {
                        Toast.makeText(this,"update successfull",Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        //catch error
                        Log.e("error :",it.exception.toString())
                    }
                }

            }
//            val intent = Intent(this, SigninActivity::class.java)
//            startActivity(intent)
//            finish()
        }


        binding.btnDeleteAcc.setOnClickListener{
            val user=Firebase.auth.currentUser
            user?.delete()?.addOnCompleteListener{
                if (it.isSuccessful)
                {
                    //acc delete
                    Toast.makeText(this,"delete acc successfully",Toast.LENGTH_SHORT).show()
                    //after deteleint the acc
                    val intent=Intent(this,SigninActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    Log.e("error : ",it.exception.toString())
                }

            }
        }
    }
    private fun checkpassword():Boolean
    {
        if (binding.signinEtpass.text.toString()==""){
            binding.signinTextInputLayoutpass.error="this is required fill"
            binding.signinTextInputLayoutpass.errorIconDrawable=null
            return false
        }

        if (binding.signinEtpass.length()<=6){
            binding.signinTextInputLayoutpass.error="pass at least 6 character"
            binding.signinTextInputLayoutpass.errorIconDrawable=null
            return false
        }
        return true

    }
}
