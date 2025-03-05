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

        /// initialize the firebase auth
        auth = FirebaseAuth.getInstance()
        databaseref = FirebaseDatabase.getInstance().getReference()

        /// set the user data
        setUserData()

        // userDetails from fireBase
        val intent = intent
        name = intent.getStringArrayListExtra("foodName")!!
        foodprice = intent.getStringArrayListExtra("foodprice")!!
        fooddes = intent.getStringArrayListExtra("fooddescription")!!
        foodingr = intent.getStringArrayListExtra("foodingredient")!!
        foodImage = intent.getStringArrayListExtra("foodimage")!!
        quantities = intent.getStringArrayListExtra("quantities")!!

        totalAmount = calculateTotalAmount().toString() + "$"
        binding.totalPrice.setText(totalAmount)

        binding.backbutton.setOnClickListener { finish() }
        binding.placeorderBtn.setOnClickListener {
            // get the data from the text view
            val name = binding.name.text.toString().trim()
            val address = binding.address.text.toString().trim()
            val phone = binding.phone.text.toString().trim()

            if (name.isBlank() || address.isBlank() || phone.isBlank()) {
                Toast.makeText(this, "please enter all the details", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }

            val bottomSheetDialog = CongratsFragment()
            bottomSheetDialog.show(supportFragmentManager, "Test")
        }
    }

    private fun placeOrder() {
        userID = auth.currentUser?.uid.toString()
        val currentTime = System.currentTimeMillis()
        val itemPushKey = databaseref.child("OrderDetails").push().key/* .push() → Creates a unique child node with an auto-generated key.
.key → Retrieves the key of that newly generated node.  */
        val orderDetails = orderDetails(
            userID,
            name.toString(),
            foodname,
            foodImage,
            foodprice,
            quantities,
            address.toString(),
            currentTime.toString(),
            phone.toString(),
            currentTime = currentTime,
            itemPushKey = itemPushKey,
            orderAccepted = false,
            paymentReceived = false
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
            .child(orderDetails.itemPushKey!!).setValue(orderDetails).addOnSuccessListener {

            }
    }

    private fun removeItemfromCart() {
        val cartItemsRef = databaseref.child("users").child(userID).child("CartItems")
        cartItemsRef.removeValue()
        startActivity(Intent(this, HistoryFragment::class.java))
    }

    private fun calculateTotalAmount(): Int {
        var totalamount = 0
        for (i in 0 until foodprice.size) {
            val price = foodprice[i]
            val lastchar = price.last()
            val priceIntvalue = if (lastchar == '$') {
                price.dropLast(1).toInt()
            } else {
                price.toInt()
            }
            val quantity = quantities[i]

            totalamount = totalamount + (price.toInt() * quantity.toInt())

        }
        return totalamount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userRef = databaseref.child("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val Username = snapshot.child("name").getValue(String::class.java)
                        val Userphone = snapshot.child("phone").getValue(String::class.java)
                        val Useraddress = snapshot.child("address").getValue(String::class.java)

                        binding.apply {
                            name.setText(Username)
                            address.setText(Useraddress)
                            phone.setText(Userphone)
                        }
                    }

                }


                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}