package com.example.food_app.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_app.dataModel.menuItem
import com.example.food_app.databinding.MenuRvItemsBinding

import com.example.food_app.itemDetailsActivity
import com.example.food_app.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class menuAdapter(
    private val menuitems: List<menuItem>,

    private val requiecontext: Context,

    ) : RecyclerView.Adapter<menuAdapter.menuviewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): menuAdapter.menuviewHolder {
        val binding = MenuRvItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return menuviewHolder(binding)
    }

    override fun onBindViewHolder(holder: menuAdapter.menuviewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return menuitems.size // for the size of the list
    }

    inner class menuviewHolder(private val binding: MenuRvItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init { // for the click listener of the items , when the view-holder is clicked it will go to the itemdetailsactivity
            binding.root.setOnClickListener {
                val position = adapterPosition // for the position of the item
                if (position != RecyclerView.NO_POSITION) { // for the position of the item , no_position is used to check if the item is valid or not
                    Log.d("menuAdapter", "Item clicked at position: $position")
                    openDetailActivity(position) // for the click listener of the items
                }
            }
        }

        private fun openDetailActivity(position: Int) {
            val menuitem = menuitems[position]
            Log.d("menuAdapter", "Opening detail activity for item: ${menuitem.foodname}")
            val intent = Intent(requiecontext, itemDetailsActivity::class.java)
                .apply {
                    putExtra("name", menuitem.foodname)
                    putExtra("imageurl", menuitem.foodImageurl ?: "")
                    putExtra("price", menuitem.foodPrice)
                    putExtra("description", menuitem.foodDescription)
                    putExtra("ingridient", menuitem.foodIndgredents)
                }
            requiecontext.startActivity(intent)

        }

        fun bind(position: Int) {
            binding.apply {
                menuitemname.text = menuitems[position].foodname
                menuitemprice.text = menuitems[position].foodPrice
                if (menuitems[position].foodImageurl != null) {
                    val uri = Uri.parse(menuitems[position].foodImageurl)
                    Glide.with(requiecontext).load(uri).into(menuitemimage)
                }

                menuitemaddtocartbtn.setOnClickListener {
                    val menuItem = menuitems[position]
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        FirebaseDatabase.getInstance().reference
                            .child("cart")
                            .child(userId)
                            .push()
                            .setValue(menuItem)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requiecontext,
                                    "${menuItem.foodname} added to cart",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    requiecontext,
                                    "Failed to add to cart",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(requiecontext, "User not logged in", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
    }

}

