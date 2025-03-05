package com.example.food_app.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_app.databinding.HistoryRvItemBinding

class BuyagainAdapter(
    private val itemnames: MutableList<String>,
    private val itemprices: MutableList<String>,
    private val itemimages: MutableList<String>,
    private val requireContext: Context,
) : RecyclerView.Adapter<BuyagainAdapter.viewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): viewHolder {
        val binding =
            HistoryRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: viewHolder,
        position: Int,
    ) {
        holder.bind(itemnames[position], itemprices[position], itemimages[position])
    }

    override fun getItemCount(): Int {
        return itemnames.size
    }


    inner class viewHolder(private val binding: HistoryRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            itemname: String,
            itemprice: String,
            itemimage: String,
        ) {

            binding.historyitemname.text = itemname
            binding.historyitemprice.text = itemprice
            val uri = Uri.parse(itemimage)
            Glide.with(requireContext).load(uri).into(binding.historyitemimage)

        }
    }
}