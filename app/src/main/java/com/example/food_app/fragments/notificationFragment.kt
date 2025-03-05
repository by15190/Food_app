package com.example.food_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_app.R
import com.example.food_app.adapter.notificationAdapter
import com.example.food_app.databinding.FragmentNotificationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class notificationFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var adapter: notificationAdapter

    /// dummy data
    private val notifcation_texts = listOf<String>(
        "Your order has been Canceled Successfully",
        "Order has been taken by the driver",
        "Congrats Your Order Placed"
    )

    private val notifcation_images =
        listOf<Int>(R.drawable.sademoji, R.drawable.truck, R.drawable.congrats)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // adapter
        adapter = notificationAdapter(notifcation_images, notifcation_texts)

        // recycler view
        binding.notificationRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.notificationRecyclerview.adapter = adapter


    }
}