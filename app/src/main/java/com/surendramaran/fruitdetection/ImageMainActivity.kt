package com.surendramaran.fruitdetection

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ImageMainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var buttons: FloatingActionButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_image_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Check if supportActionBar is not null
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLUE))

        // Initialize views
        imageView = findViewById(R.id.personview)
        buttons = findViewById(R.id.floatingActionButton)

        // Set button click listener
        buttons.setOnClickListener {
            ImagePicker.with(this)
                .crop() // Crop image(Optional), Check Customization for more option
                .compress(1024) // Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080) // Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        val button = findViewById<Button>(R.id.buttongoImage)
        button.setOnClickListener {
            val intent = Intent(this, ImageViewActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageView.setImageURI(data?.data)
    }
}
