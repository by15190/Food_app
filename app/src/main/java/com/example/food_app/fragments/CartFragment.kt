package com.example.food_app.fragments

import android.content.Intent
import android.os.Bundle
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize the firebase auth
        auth = FirebaseAuth.getInstance()
        retreiveCartItems()


        // setting the cart process btn
        binding.ProcessCartButton.setOnClickListener {
            // get order items details before proceeding to check out
            getorderItemDetail()


        }
    }

    private fun getorderItemDetail() {
        val orderIdref: DatabaseReference =
            database.reference.child("users").child(userID).child("cartItems")

        val foodName = mutableListOf<String>()
        val foodimage = mutableListOf<String>()
        val foodprice = mutableListOf<String>()
        val fooddescription = mutableListOf<String>()
        val foodingredient = mutableListOf<String>()

        // get the item quantities
        val foodQuatities = cartAdapter.getUpdatedItemsQuantities()

        orderIdref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnap in snapshot.children) {
                    // get the cartItems to respective lists
                    val orderItems = foodsnap.getValue(cartItem::class.java)
                    // add the items to the respective lists
                    orderItems?.foodname.let { foodName.add(it.toString()) }
                    orderItems?.foodprice.let { foodprice.add(it.toString()) }
                    orderItems?.fooddescription.let { fooddescription.add(it.toString()) }
                    orderItems?.foodimageurl.let { foodimage.add(it.toString()) }
                    orderItems?.foodingredient.let { foodingredient.add(it.toString()) }
                }
                orderNow(
                    foodName, foodprice, fooddescription, foodimage, foodingredient, foodQuatities
                )
            }

            private fun orderNow(
                foodName: MutableList<String>,
                foodprice: MutableList<String>,
                fooddescription: MutableList<String>,
                foodimage: MutableList<String>,
                foodingredient: MutableList<String>,
                quantities: MutableList<Int>,
            ) {
                if (isAdded && context != null) {
                    val intent = Intent(requireContext(), payoutActivity::class.java)
                    intent.putExtra("foodName", foodName as ArrayList<String>)
                    intent.putExtra("foodprice", foodprice as ArrayList<String>)
                    intent.putExtra("fooddescription", fooddescription as ArrayList<String>)
                    intent.putExtra("foodingredient", foodingredient as ArrayList<String>)
                    intent.putExtra("foodimage", foodimage as ArrayList<String>)
                    intent.putExtra("quantities", quantities as ArrayList<String>)

                    startActivity(intent)
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun retreiveCartItems() {
        database = FirebaseDatabase.getInstance()
        userID = auth.currentUser?.uid.toString()
        val foodref = database.getReference("Cart").child(userID).child("cartItems")

        // list to store cart items
        foodnames = mutableListOf()
        foodprices = mutableListOf()
        fooddescription = mutableListOf()
        foodingredients = mutableListOf()
        foodimageUri = mutableListOf()
        quantity = mutableListOf()

        // fetch the data from the database
        foodref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnap in snapshot.children) {
                    val cartitems = foodsnap.getValue(cartItem::class.java)

                    cartitems?.foodname.let { foodnames.add(it.toString()) }
                    cartitems?.foodprice.let { foodprices.add(it.toString()) }
                    cartitems?.fooddescription.let { fooddescription.add(it.toString()) }
                    cartitems?.quantity.let { quantity.add(it!!) }
                    cartitems?.foodimageurl.let { foodimageUri.add(it.toString()) }
                    cartitems?.foodingredient.let { foodingredients.add(it.toString()) }
                }

                setAdapter()
            }

            private fun setAdapter() {
                val adapter = cartAdapter(
                    requireContext(),
                    foodnames,
                    foodprices,
                    foodimageUri,
                    quantity,
                    fooddescription,
                    foodingredients
                )
                // setting the adapter to the recycler view
                binding.cartRv.layoutManager = LinearLayoutManager(requireContext())
                binding.cartRv.adapter = adapter


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "data not fetch", Toast.LENGTH_SHORT).show()
            }

        })
    }

}
