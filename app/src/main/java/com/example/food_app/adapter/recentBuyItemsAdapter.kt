package com.example.food_app.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_app.databinding.OrderhistoryitemsRvItemBinding


class recentBuyItemsAdapter(
    private val context: Context,
    private val foodnameList: List<String>,
    private val foodimagesList: List<String>,
    private val foodpricesList: List<String>,
    private val foodquantitiesList: List<String>,
) : RecyclerView.Adapter<recentBuyItemsAdapter.viewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): viewHolder {
        val binding =
            OrderhistoryitemsRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return viewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: viewHolder,
        position: Int,
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return foodnameList.size
    }

    inner class viewHolder(private val binding: OrderhistoryitemsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                itemname.text = foodnameList[position]
                itemprice.text = foodpricesList[position]
                itemQuantity.text = foodquantitiesList[position]
                val uri = Uri.parse(foodimagesList[position])
                Glide.with(context).load(uri).into(itemimage)
            }
        }
    }
}