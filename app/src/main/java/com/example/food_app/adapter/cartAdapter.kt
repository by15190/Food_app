package com.example.food_app.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_app.databinding.CartRvItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class cartAdapter(
    private val context: Context,
    private val cartitems: MutableList<String>,
    private val cartitemPrices: MutableList<String>,
    private val cartitemImages: MutableList<String>,
    private var cartitemQuantities: MutableList<Int>,
    private var cartitemDescription: MutableList<String>,
    private var cartitemIngredient: MutableList<String>,
    private val cartItemKeys: MutableList<String>
) : RecyclerView.Adapter<cartAdapter.cartviewHolder>() {

    private val auth = FirebaseAuth.getInstance()
    private val cartItemsRef = FirebaseDatabase.getInstance()
        .reference.child("cart")
        .child(auth.currentUser?.uid ?: "")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartviewHolder {
        val binding = CartRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return cartviewHolder(binding)
    }

    override fun onBindViewHolder(holder: cartviewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartitems.size

    fun getUpdatedItemsQuantities(): MutableList<Int> {
        return cartitemQuantities.toMutableList()
    }

    inner class cartviewHolder(private val binding: CartRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                cartItemname.text = cartitems[position]
                cartItemprice.text = cartitemPrices[position].toString()

                Glide.with(context).load(Uri.parse(cartitemImages[position])).into(cartItemimage)
                cartItemQuantity.text = cartitemQuantities[position].toString()

                cartMinusbtn.setOnClickListener { decrease(position) }
                cartPlusbtn.setOnClickListener { increase(position) }

                cartdeletebtn.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        delete(itemPosition)
                    }
                }
            }
        }

        private fun delete(position: Int) {
            val key = cartItemKeys.getOrNull(position)
            if (key != null) {
                removeCartItem(key, position)
            } else {
                Toast.makeText(context, "Unable to find item in DB", Toast.LENGTH_SHORT).show()
            }
        }

        private fun removeCartItem(uniqueKey: String, position: Int) {
            cartItemsRef.child(uniqueKey).removeValue().addOnSuccessListener {
                cartitems.removeAt(position)
                cartitemPrices.removeAt(position)
                cartitemDescription.removeAt(position)
                cartitemImages.removeAt(position)
                cartitemIngredient.removeAt(position)
                cartitemQuantities.removeAt(position)
                cartItemKeys.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, cartitems.size)
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to remove the item from the cart", Toast.LENGTH_SHORT).show()
            }
        }

        private fun decrease(position: Int) {
            if (cartitemQuantities[position] > 1) {
                cartitemQuantities[position]--
                binding.cartItemQuantity.text = cartitemQuantities[position].toString()
            }
        }

        private fun increase(position: Int) {
            if (cartitemQuantities[position] < 10) {
                cartitemQuantities[position]++
                binding.cartItemQuantity.text = cartitemQuantities[position].toString()
            }
        }
    }
}
