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


    ) :
    RecyclerView.Adapter<cartAdapter.cartviewHolder>() {
    // initialize the firebase auth and the database
    private val auth = FirebaseAuth.getInstance()

    init {
        val database = FirebaseDatabase.getInstance()
        val userID = auth.currentUser?.uid
        val cartItemNumber = cartitems.size

        val itemQuantities = IntArray(cartItemNumber) { 1 }
        /* initializes an IntArray with the same size as cart-items, setting each element to 1.
        This ensures that every item in the cart starts with a default quantity of 1.
        */
        cartItemsRef = database.reference.child("users").child(userID!!).child("CartItems")
    }

    companion object {
        private val itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemsRef: DatabaseReference
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): cartviewHolder {
        val binding: CartRvItemBinding =
            CartRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return cartviewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: cartviewHolder,
        position: Int,
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return cartitems.size
    }

    fun getUpdatedItemsQuantities(): MutableList<Int> {
        val itemquantities = mutableListOf<Int>()
        itemquantities.addAll(cartitemQuantities)
        return itemquantities
    }

    inner class cartviewHolder(private val binding: CartRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val itemQuantity =
                    cartitemQuantities[position] // get the current quantity for the item at the given position

                cartItemname.text = cartitems[position]
                cartItemprice.text = cartitemPrices[position]
                // image of the item
                val uri = Uri.parse(cartitemImages[position])
                Glide.with(context).load(uri).into(cartItemimage)
                cartItemQuantity.text = itemQuantity.toString()

                // minus button
                cartMinusbtn.setOnClickListener {
                    decrease(position)
                }


                // plus button
                cartPlusbtn.setOnClickListener {
                    increace(position)

                }

                // delete button
                cartdeletebtn.setOnClickListener {
                    val itemPosition =
                        adapterPosition // get the position of the item in the adapter
                    if (itemPosition != RecyclerView.NO_POSITION) { // check if the item position is valid
                        delete(position)
                    }

                }
            }


        }

        // fun to delete the item from the cart item list
        private fun delete(position: Int) {
            val positionRetrive = position
            getUniqueKeyatPosition(positionRetrive) { uniqueKey ->
                if (uniqueKey != null) {
                    removeCartItem(uniqueKey, position)
                }
            }

        }

        private fun removeCartItem(uniqueKey: String, position: Int) {
            if (uniqueKey != null) {
                cartItemsRef.child(uniqueKey).removeValue().addOnSuccessListener {
                    cartitems.removeAt(position)
                    cartitemPrices.removeAt(position)
                    cartitemDescription.removeAt(position)
                    cartitemImages.removeAt(position)
                    cartitemIngredient.removeAt(position)
                    cartitemQuantities.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartitems.size)
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Failed to remove the item from the cart",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        private fun getUniqueKeyatPosition(positionRetrive: Int, oncomplete: (String) -> Unit) {
            cartItemsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null

                    snapshot.children.forEachIndexed { index, datasnapshot ->
                        if (index == positionRetrive) {
                            uniqueKey = datasnapshot.key
                            return@forEachIndexed
                        }
                    }
                    oncomplete(uniqueKey!!)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


        // fun to decrease the quantity of the item
        private fun decrease(position: Int) {
            if (cartitemQuantities[position] > 1) {
                cartitemQuantities[position]--

                binding.cartItemQuantity.text = cartitemQuantities[position].toString()
            }
        }

        // fun to increase the quantity of the item
        private fun increace(position: Int) {
            if (cartitemQuantities[position] < 10) {
                cartitemQuantities[position]++
                binding.cartItemQuantity.text = cartitemQuantities[position].toString()
            }
        }


    }
}