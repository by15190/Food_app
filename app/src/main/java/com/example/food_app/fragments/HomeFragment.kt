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
import com.example.food_app.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {
    // View binding for accessing XML views
    private lateinit var binding: FragmentHomeBinding

    // Firebase database reference
    private lateinit var database: FirebaseDatabase

    // List to store menu items retrieved from Firebase
    private lateinit var menuItems: MutableList<menuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment using view binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Image list for the slider (banners)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        // Set up image slider with the image list
        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)

        // Image click listener for slider
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                // TODO: Implement double-click behavior if needed
            }

            override fun onItemSelected(position: Int) {
                // Display the clicked image position as a Toast message
                val itemMessage = "Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch menu data and display popular items
        retrieveMenuAndDisplayPopularItems()

        // Handle click event on "View Full Menu" button
        binding.viewfullmenu.setOnClickListener {
            val bottomSheetDialog = menu_bottom_sheet_Fragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }
    }

    // Function to retrieve menu items from Firebase and display them
    private fun retrieveMenuAndDisplayPopularItems() {
        // Initialize Firebase database instance
        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menus")
        menuItems = mutableListOf() // Initialize the list

        // Fetch menu data from Firebase
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Loop through each menu item in Firebase
                for (foodSnapshot in snapshot.children) {
                    val menuItem =
                        foodSnapshot.getValue(menuItem::class.java) // Convert snapshot to MenuItem object
                    menuItem?.let {
                        menuItems.add(it) // Add non-null menu items to the list
                    }
                }
                // Once data is fetched, set up the RecyclerView adapter
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // Function to set up RecyclerView with menu items
    private fun setAdapter() {
        val adapter = menuAdapter(menuItems, requireContext()) // Initialize adapter
        binding.popularrecyclerView.layoutManager =
            LinearLayoutManager(requireContext()) // Set layout manager
        binding.popularrecyclerView.adapter = adapter // Attach adapter to RecyclerView
    }
}
