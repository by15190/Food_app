package com.example.food_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.food_app.databinding.PopularRvItemBinding
import com.example.food_app.itemDetailsActivity


class popularAdapter(
    private val items: List<String>,
    private val prices: List<String>,
    private val images: List<Int>,
    private val requirecontext: Context,

    ) : RecyclerView.Adapter<popularAdapter.popularViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): popularViewHolder {
        return popularViewHolder(
            PopularRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: popularViewHolder,
        position: Int,
    ) {
        val item = items[position]
        val image = images[position]
        val price = prices[position]

        holder.bind(item, price, image)

        holder.itemView.setOnClickListener{
            val intent = Intent(requirecontext, itemDetailsActivity::class.java)
            intent.putExtra("menuitemname", item)
            intent.putExtra("menuitemimage", image)
            requirecontext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class popularViewHolder(private val binding: PopularRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {



        fun bind(item: String, price: String, image: Int) {
            binding.popularitemname.text = item
            binding.popularitemprice.text = price
            binding.popularitemimage.setImageResource(image)


        }

    }
}

