package com.example.food_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.food_app.R
import com.example.food_app.adapter.menuAdapter
import com.example.food_app.adapter.popularAdapter
import com.example.food_app.dataModel.menuItem
import com.example.food_app.databinding.CartRvItemBinding
import com.example.food_app.databinding.FragmentMenuBottomSheetBinding
import com.example.food_app.databinding.MenuRvItemsBinding
import com.example.food_app.fragments.menu_bottom_sheet_Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/*
this is for the view more option in the home page fragment

 */


class menu_bottom_sheet_Fragment :
    BottomSheetDialogFragment() { // bottomsheetdialogfragment  is used to show the bottom sheet dialog
    // ViewBinding for the fragment
    private lateinit var binding: FragmentMenuBottomSheetBinding

    // Firebase Database reference
    private lateinit var database: FirebaseDatabase

    // List to store menu items
    private val menuItems: MutableList<menuItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout using ViewBinding
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve menu items from Firebase
        retrieveMenuItems()

        // Handle back button click to dismiss the bottom sheet
        binding.backImageButton.setOnClickListener {
            dismiss()
        }
    }

    private fun retrieveMenuItems() {
        // Get an instance of Firebase database
        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menu")

        // Fetch data from Firebase
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the list to avoid duplication
                menuItems.clear()

                // Iterate through the retrieved data and add items to the list
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(menuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }

                // Once data is retrieved, set up the RecyclerView adapter
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setAdapter() {
        // Create adapter instance with retrieved menu items
        val adapter = menuAdapter(menuItems, requireContext())

        // Set layout manager and adapter for RecyclerView
        binding.menuSheetRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.menuSheetRecyclerview.adapter = adapter
    }
}
