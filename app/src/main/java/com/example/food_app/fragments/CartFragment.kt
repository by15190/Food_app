package com.example.food_app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_app.adapter.cartAdapter
import com.example.food_app.dataModel.cartItem
import com.example.food_app.databinding.FragmentCartBinding
import com.example.food_app.payoutActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodnames: MutableList<String>
    private lateinit var foodprices: MutableList<String>
    private lateinit var fooddescription: MutableList<String>
    private lateinit var foodimageUri: MutableList<String>
    private lateinit var foodingredients: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: cartAdapter
    private lateinit var userID: String
    val itemKeys = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        retreiveCartItems()

        binding.ProcessCartButton.setOnClickListener {
            getorderItemDetail()
        }
    }

    private fun getorderItemDetail() {
        Log.d("CartFragment", "getorderItemDetail() called")
        val orderIdref: DatabaseReference = database.reference.child("cart").child(userID)
        val foodName = mutableListOf<String>()
        val foodimage = mutableListOf<String?>()
        val foodprice = mutableListOf<String>()
        val fooddescription = mutableListOf<String>()
        val foodingredient = mutableListOf<String>()

        val foodQuantities = cartAdapter?.getUpdatedItemsQuantities() ?: mutableListOf()

        orderIdref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnap in snapshot.children) {
                    val orderItems = foodsnap.getValue(cartItem::class.java)
                    foodName.add(orderItems?.foodname ?: "")
                    val price = orderItems?.foodPrice
                    foodprice.add(if (price == null || price == "null") "0" else price)
                    fooddescription.add(orderItems?.foodDescription ?: "")
                    foodimage.add(orderItems?.foodImageurl ?: "")
                    foodingredient.add(orderItems?.foodIngredient ?: "")
                }
                orderNow(foodName, foodprice, fooddescription, foodimage, foodingredient, foodQuantities)
            }

            private fun orderNow(
                foodName: MutableList<String>,
                foodprice: MutableList<String>,
                fooddescription: MutableList<String>,
                foodimage: MutableList<String?>,
                foodingredient: MutableList<String>,
                quantities: MutableList<Int>
            ) {
                if (isAdded && context != null) {
                    val intent = Intent(requireContext(), payoutActivity::class.java)
                    intent.putExtra("foodName", ArrayList(foodName))
                    intent.putExtra("foodprice", ArrayList(foodprice))
                    intent.putExtra("fooddescription", ArrayList(fooddescription))
                    intent.putExtra("foodingredient", ArrayList(foodingredient))
                    intent.putExtra("foodimage", ArrayList(foodimage))
                    intent.putExtra("quantities", ArrayList(quantities.map { it.toString() }))
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to get order items", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun retreiveCartItems() {
        database = FirebaseDatabase.getInstance()
        userID = auth.currentUser?.uid.toString()
        val foodref = database.getReference("cart").child(userID)

        foodnames = mutableListOf()
        foodprices = mutableListOf()
        fooddescription = mutableListOf()
        foodingredients = mutableListOf()
        foodimageUri = mutableListOf()
        quantity = mutableListOf()
        itemKeys.clear()

        foodref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnap in snapshot.children) {
                    val cartitems = foodsnap.getValue(cartItem::class.java)
                    Log.d("CartFragment", "Item snapshot: ${foodsnap.key} -> ${foodsnap.value}")

                    itemKeys.add(foodsnap.key ?: "")

                    val name = cartitems?.foodname ?: ""
                    val price = cartitems?.foodPrice
                    val desc = cartitems?.foodDescription ?: ""
                    val quant = cartitems?.quantity ?: 1
                    val img = cartitems?.foodImageurl ?: ""
                    val ing = cartitems?.foodIngredient ?: ""

                    foodnames.add(name)
                    foodprices.add(if (price == null || price == "null") "0" else price)
                    fooddescription.add(desc)
                    quantity.add(quant)
                    foodimageUri.add(img)
                    foodingredients.add(ing)
                }

                setAdapter()
            }

            private fun setAdapter() {
                cartAdapter = cartAdapter(
                    requireContext(),
                    foodnames,
                    foodprices,
                    foodimageUri,
                    quantity,
                    fooddescription,
                    foodingredients,
                    cartItemKeys = itemKeys
                )
                binding.cartRv.layoutManager = LinearLayoutManager(requireContext())
                binding.cartRv.adapter = cartAdapter

                Log.d("CartFragment", "Adapter set with ${foodnames.size} items")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CartFragment", "Failed to fetch cart items: ${error.message}")
                Toast.makeText(requireContext(), "Data not fetch", Toast.LENGTH_SHORT).show()
            }
        })
    }
}