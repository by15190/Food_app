package com.example.food_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.food_app.dataModel.cartItem
import com.example.food_app.databinding.ActivityItemDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class itemDetailsActivity : AppCompatActivity() {
    private var foodName: String? = null
    private var foodprice: String? = null
    private var foodimage: String? = null
    private var fooddescription: String? = null
    private var foodindgredient: String? = null
    private lateinit var auth: FirebaseAuth


    private val binding: ActivityItemDetailsBinding by lazy {
        ActivityItemDetailsBinding.inflate(
            layoutInflater
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // initialize the auth
        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // back btn
        binding.backBtn.setOnClickListener {
            finish()
        }

        foodName = intent.getStringExtra("name")
        fooddescription = intent.getStringExtra("description")
        foodprice = intent.getStringExtra("price")
        foodindgredient = intent.getStringExtra("ingridient")
        foodimage = intent.getStringExtra("imageurl")?:""

        with(binding) {
            itemDetailFoodname.text = foodName
            itemDetailFooddescription.text = fooddescription
            itemDetailFoodIngredients.text = foodindgredient
            if (foodimage != null){
            val uri = Uri.parse(foodimage)
            Glide.with(this@itemDetailsActivity).load(uri).into(itemDetailFoodimage)
        }
        }

        binding.addtoCartbutton.setOnClickListener {
            additemtoCart()
        }
    }

    private fun additemtoCart() {
        // we will store the cart data with the userData in the Firebase
        val database = FirebaseDatabase.getInstance().reference
        val userID = auth.currentUser?.uid // get current user id

        // create the item to be added to the cart
        val cartItem = cartItem(
            foodimage.toString(),
            foodName.toString(),
            fooddescription.toString(),
            1,
            foodprice.toString()
        )
// save the cartItem to the database in usersData
        database.child("users").child(userID.toString()).child("CartItems")
            .push() // the push will create a unique key for each item
            .setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Item added to the Cart", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "failed to add the item in the cart",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

}