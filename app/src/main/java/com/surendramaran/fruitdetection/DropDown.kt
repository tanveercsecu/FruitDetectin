package com.surendramaran.fruitdetection

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DropDown : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_drop_down)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner: Spinner = findViewById(R.id.spinner)
        val buttonNavigate: Button = findViewById(R.id.button_navigate)

        // Sample data for the dropdown
        val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")

        // Adapter for Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        buttonNavigate.setOnClickListener {
            // Get selected item from Spinner
            val selectedItem = spinner.selectedItem.toString()

            // Intent to navigate to SecondActivity
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("SELECTED_ITEM", selectedItem)
            startActivity(intent)
        }
    }
}