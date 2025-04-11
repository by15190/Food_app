package com.example.food_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food_app.dataModel.orderDetails
import com.example.food_app.databinding.ActivityPayoutBinding
import com.example.food_app.fragments.HistoryFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class payoutActivity : AppCompatActivity() {
    private val binding: ActivityPayoutBinding by lazy {
        ActivityPayoutBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var name: ArrayList<String>
    private lateinit var address: ArrayList<String>
    private lateinit var phone: ArrayList<String>
    private lateinit var totalAmount: String
    private lateinit var foodname: ArrayList<String>
    private lateinit var foodprice: ArrayList<String>
    private lateinit var foodImage: ArrayList<String>
    private lateinit var fooddes: ArrayList<String>
    private lateinit var foodingr: ArrayList<String>
    private lateinit var quantities: ArrayList<String>

    private lateinit var databaseref: DatabaseReference
    private lateinit var userID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance().reference

        // Pre-fill user data
        setUserData()

        // Get order data from intent
        foodname = intent.getStringArrayListExtra("foodName")!!
        foodprice = intent.getStringArrayListExtra("foodprice")!!
        fooddes = intent.getStringArrayListExtra("fooddescription")!!
        foodingr = intent.getStringArrayListExtra("foodingredient")!!
        foodImage = intent.getStringArrayListExtra("foodimage")!!
        quantities = intent.getStringArrayListExtra("quantities")!!

        totalAmount = calculateTotalAmount().toString() + "$"
        binding.totalPrice.setText(totalAmount)

        binding.backbutton.setOnClickListener { finish() }

        binding.placeorderBtn.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val address = binding.address.text.toString().trim()
            val phone = binding.phone.text.toString().trim()

            if (name.isBlank() || address.isBlank() || phone.isBlank()) {
                Toast.makeText(this, "please enter all the details", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
                val bottomSheetDialog = CongratsFragment()
                bottomSheetDialog.show(supportFragmentManager, "Test")
            }
        }
    }

    private fun placeOrder() {
        userID = auth.currentUser?.uid.toString()
        val currentTime = System.currentTimeMillis()
        val itemPushKey = databaseref.child("OrderDetails").push().key

        val orderDetails = orderDetails(
            userUID = userID,
            userName = binding.name.text.toString().trim(),
            foodNames = foodname,
            foodImages = foodImage ,
            foodPrices = foodprice,
            foodQuantities = quantities,
            address = binding.address.text.toString().trim(),
            totalPrice = totalAmount,
            phone = binding.phone.text.toString().trim(),
            orderAccepted = false,
            paymentReceived = false,
            itemPushKey = itemPushKey,
            currentTime = currentTime
        )

        val orderRef = databaseref.child("OrderDetails").child(itemPushKey!!)
        orderRef.setValue(orderDetails).addOnSuccessListener {
            Toast.makeText(this, "order placed successfully", Toast.LENGTH_SHORT).show()
            removeItemfromCart()
            addOrderDetailtoHistory(orderDetails)
        }.addOnFailureListener {
            Toast.makeText(this, "failed to place order", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addOrderDetailtoHistory(orderDetails: orderDetails) {
        databaseref.child("users").child(userID).child("buyHistory")
            .child(orderDetails.itemPushKey!!).setValue(orderDetails)
    }

    private fun removeItemfromCart() {
        val cartItemsRef = databaseref.child("users").child(userID).child("CartItems")
        cartItemsRef.removeValue()
        startActivity(Intent(this, HistoryFragment::class.java))
    }
    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until foodprice.size) {
            val priceStr = foodprice.getOrNull(i)?.trim() ?: continue
            val quantityStr = quantities.getOrNull(i)?.trim() ?: "1"

            // skip if price is empty
            if (priceStr.isEmpty()) continue

            val cleanPrice = priceStr.removeSuffix("$")
            val priceInt = cleanPrice.toIntOrNull() ?: 0
            val qtyInt = quantityStr.toIntOrNull() ?: 1

            totalAmount += priceInt * qtyInt
        }
        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userRef = databaseref.child("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val username = snapshot.child("name").getValue(String::class.java)
                        val userPhone = snapshot.child("phone").getValue(String::class.java)
                        val userAddress = snapshot.child("address").getValue(String::class.java)

                        binding.apply {
                            name.setText(username)
                            address.setText(userAddress)
                            phone.setText(userPhone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@payoutActivity,
                        "Error loading user data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}