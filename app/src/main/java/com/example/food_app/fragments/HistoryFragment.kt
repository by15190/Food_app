package com.example.food_app.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.food_app.R
import com.example.food_app.adapter.BuyagainAdapter
import com.example.food_app.dataModel.orderDetails
import com.example.food_app.databinding.FragmentHistoryBinding
import com.example.food_app.recentOrderItemsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.Serializable

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding // binding
    private lateinit var adapter: BuyagainAdapter // adapter
    private lateinit var database: FirebaseDatabase // database
    private lateinit var auth: FirebaseAuth // auth
    private lateinit var userID: String // userid
    private var listofOrderItems: MutableList<orderDetails> = mutableListOf() // userid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        // retrieve and display the user order History
        retriveOrderHistory()

        /// recent buy item clicked
        binding.recentbuyItem.setOnClickListener {
            seeItemsRecentBuy()
        }

        binding.apply {


        }
    }

    private fun seeItemsRecentBuy() {
        listofOrderItems.firstOrNull().let { recentBuy ->
            val intent = Intent(requireContext(), recentOrderItemsActivity::class.java)
            intent.putExtra("recentBuyItems", listofOrderItems as Serializable)

            startActivity(intent)
        }
    }

    private fun retriveOrderHistory() {
        binding.recentbuyItem.visibility = View.INVISIBLE
        userID = auth.currentUser?.uid.toString()

        val buyItemREF = database.reference.child("users").child(userID).child("orders")
        val shortingQuery = buyItemREF.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapShot in snapshot.children) {
                    val buyHistoryItem = buySnapShot.getValue(orderDetails::class.java)
                    buyHistoryItem.let {
                        listofOrderItems.add(it!!)
                    }
                    listofOrderItems.reversed()
                    if (listofOrderItems.isNotEmpty()) {
                        setDataInRecentBuyItem() // recent buy item
                        setPreviousBuyItem() // previous buy item
                    }
                }
            }

            private fun setDataInRecentBuyItem() {
                binding.recentbuyItem.visibility = View.VISIBLE
                val recentBuyItem = listofOrderItems.firstOrNull()
                recentBuyItem.let {
                    with(binding) {
                        binding.historyRecentbuyFoodname.text = it?.foodNames?.firstOrNull()
                        binding.historyRecentbuyPrice.text = it?.foodPrices?.firstOrNull()
                        val imageURI = it?.foodImages?.firstOrNull()
                        val uri = Uri.parse(imageURI)
                        Glide.with(requireContext()).load(uri).into(binding.recentBuyFoodimage)

                        listofOrderItems.reversed()
                        if (listofOrderItems.isNotEmpty()) {

                        }
                    }
                }
            }

            private fun setPreviousBuyItem() {
                val foodnames = mutableListOf<String>()
                val prices = mutableListOf<String>()
                val images = mutableListOf<String>()
                for (i in 1 until listofOrderItems.size) {
                    listofOrderItems[i].foodNames.firstOrNull().let {
                        foodnames.add(it.toString())
                    }
                    listofOrderItems[i].foodPrices.firstOrNull().let {
                        prices.add(it.toString())
                    }
                    listofOrderItems[i].foodImages.firstOrNull().let {
                        images.add(it.toString())
                    }
                }

                // adapter
                adapter = BuyagainAdapter(foodnames, prices, images, requireContext())

                // recycler view
                binding.historyRecycleview.layoutManager = LinearLayoutManager(context)
                binding.historyRecycleview.adapter = adapter


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}
