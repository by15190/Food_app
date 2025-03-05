package com.example.food_app.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food_app.databinding.FragmentNotificationBinding
import com.example.food_app.databinding.NotificationRvItemBinding
import java.util.random.RandomGenerator

class notificationAdapter(private val images: List<Int>, private val texts: List<String>) :
    RecyclerView.Adapter<notificationAdapter.viewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): viewHolder {
        val binding =
            NotificationRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: viewHolder,
        position: Int,
    ) {
        holder.bind(images[position], texts[position])
    }

    override fun getItemCount(): Int {
        return texts.size
    }

    class viewHolder(private val binding: NotificationRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Int, text: String) {
            binding.notificationImageview.setImageResource(image)
            binding.notificationTextview.text = text

        }
    }
}