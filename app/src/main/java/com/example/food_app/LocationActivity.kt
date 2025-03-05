package com.example.food_app

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food_app.databinding.ActivityLocationBinding

class LocationActivity : AppCompatActivity() {
    private val binding: ActivityLocationBinding by lazy {
        ActivityLocationBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val locationList = arrayOf(
            "Jaipur",
            "Delhi",
            "Mumbai",
            "Kolkata",
            "Chennai"
        ) //  dummy data for location list for testing
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, //  layout for the list view
            locationList
        ) //  adapter for the location list to display the data in the list view

        val autocompletetextview = binding.listoflocation
        autocompletetextview.setAdapter(adapter)
    }

}