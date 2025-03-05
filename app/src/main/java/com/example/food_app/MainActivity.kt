package com.example.food_app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.food_app.databinding.ActivityMainBinding
import com.example.food_app.fragments.notificationFragment

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy{ ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var NavController = findNavController(R.id.fragmentContainerView) //find nav controller from xml
        binding.bottomNavbar.setupWithNavController(NavController)

        // notification btn
        binding.notificationimg.setOnClickListener{
            val bottomSheet = notificationFragment()
            bottomSheet.show(supportFragmentManager, "bottomSheet")
        }
    }
}


/// using image slider for the banner
/*
The NavController keeps track of the navigation stack (backstack) and helps switch between fragments smoothly.

 */
