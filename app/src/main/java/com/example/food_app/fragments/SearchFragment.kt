package com.example.food_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_app.R
import com.example.food_app.adapter.menuAdapter
import com.example.food_app.dataModel.menuItem
import com.example.food_app.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: menuAdapter
    private lateinit var database: FirebaseDatabase
    private val originalMenu = mutableListOf<menuItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val filteredname = mutableListOf<String>()
    private val filteredprice = mutableListOf<String>()
    private val filteredimage = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // retrieve menu item from the database
        retrieveMenuitem()
        // setup for search view
        setupsearchview()
    }

    private fun retrieveMenuitem() {
        database = FirebaseDatabase.getInstance()
        val datbaseref = database.reference.child("menu")
        datbaseref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnapshot in snapshot.children) {
                    if (foodsnapshot != null) {
                        val menuitem = foodsnapshot.getValue(menuItem::class.java)
                        menuitem.let { originalMenu.add(it!!) }
                    }
                }
                showallmenu()
            }

            private fun showallmenu() {
                val filterMenuitems = ArrayList(originalMenu)
                setAdapter(filterMenuitems)
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setAdapter(filterMenuitems: ArrayList<menuItem>) {

        adapter = menuAdapter(filterMenuitems, requireContext())
        binding.menurecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.menurecyclerview.adapter = adapter

    }

    private fun setupsearchview() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filtermenuitems(query)
                return true
            }


            override fun onQueryTextChange(newText: String): Boolean {
                filtermenuitems(newText)
                return true
            }


        })
    }

    private fun filtermenuitems(query: String) {
        val filtermenuitem = originalMenu.filter {
            it.foodname!!.contains(query, ignoreCase = true) == true
        }
        setAdapter(filtermenuitem as ArrayList<menuItem>)
    }
}