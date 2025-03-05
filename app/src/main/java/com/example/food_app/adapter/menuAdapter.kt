package com.example.food_app.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_app.dataModel.menuItem
import com.example.food_app.databinding.MenuRvItemsBinding

import com.example.food_app.itemDetailsActivity
import com.example.food_app.startActivity


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
                    openDetailActivity(position) // for the click listener of the items
                }
            }
        }

        private fun openDetailActivity(position: Int) {
            val menuitem = menuitems[position]

            val intent = Intent(requiecontext, itemDetailsActivity::class.java)
                .apply {
                    putExtra("name", menuitem.foodname)
                    putExtra("imageurl", menuitem.foodimageurl)
                    putExtra("price", menuitem.foodprice)
                    putExtra("description", menuitem.fooddescription)
                    putExtra("indegridient", menuitem.foodindgredient)
                }
            requiecontext.startActivity(intent)

        }

        fun bind(position: Int) {
            binding.apply {
                menuitemname.text = menuitems[position].foodname
                menuitemprice.text = menuitems[position].foodprice
                val uri = Uri.parse(menuitems[position].foodimageurl)
                Glide.with(requiecontext).load(uri).into(menuitemimage)

            }

        }

    }

}