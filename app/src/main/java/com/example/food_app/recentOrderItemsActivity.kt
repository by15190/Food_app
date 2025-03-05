package com.example.food_app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.food_app.adapter.recentBuyItemsAdapter
import com.example.food_app.dataModel.orderDetails
import com.example.food_app.databinding.ActivityRecentOrderItemsBinding

class recentOrderItemsActivity : AppCompatActivity() {
    private val binding: ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }

    private lateinit var foodNamesList: ArrayList<String>
    private lateinit var foodimagesList: ArrayList<String>
    private lateinit var foodpricesList: ArrayList<String>
    private lateinit var foodquantitiesList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.backbtn.setOnClickListener {
            finish()
        }

        // get the data from the intent
        val orderDetails = intent.getSerializableExtra("recentBuyItems") as ArrayList<orderDetails>
        orderDetails.let { oderdetails ->
            if (orderDetails!!.isNotEmpty()) {
                val recentorderItems = oderdetails?.get(0)
                foodNamesList = recentorderItems?.foodNames as ArrayList<String>
                foodimagesList = recentorderItems?.foodImages as ArrayList<String>
                foodpricesList = recentorderItems?.foodPrices as ArrayList<String>
                foodquantitiesList = recentorderItems?.foodQuantities as ArrayList<String>

            }
            setAdapter()
        }

    }

    private fun setAdapter() {
        val adapter = recentBuyItemsAdapter(
            this,
            foodNamesList,
            foodimagesList,
            foodpricesList,
            foodquantitiesList
        )
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter
    }
}