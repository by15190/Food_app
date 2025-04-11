package com.example.food_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.food_app.R
import com.example.food_app.dataModel.userData
import com.example.food_app.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        setUserData()

        binding.apply {
            profileName.isEnabled = false
            profileEmail.isEnabled = false
            profilePhone.isEnabled = false
            profileAddress.isEnabled = false
        }
        binding.editbtn.setOnClickListener {
            binding.apply {
                profileName.isEnabled = true
                profileEmail.isEnabled = true
                profilePhone.isEnabled = true
                profileAddress.isEnabled = true

            }
        }
        binding.saveProfilebtn.setOnClickListener {
            val name = binding.profileName.text.toString()
            val email = binding.profileEmail.text.toString()
            val phone = binding.profilePhone.text.toString()
            val address = binding.profileAddress.text.toString()

            updateUserData(name, email, phone, address)
        }

        return binding.root
    }

    private fun updateUserData(
        name: String,
        email: String,
        phone: String,
        address: String,
    ) {
        val userID = auth.currentUser?.uid
        if (userID != null) {
            val userRef = database.getReference("users").child(userID)
            val userData = hashMapOf(
                "name" to name, "email" to email, "phone" to phone, "address" to address
            )
            userRef.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "success to save profile", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "failed to save profile", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setUserData() {
        val userID = auth.currentUser?.uid
        if (userID != null) {
            val userref = database.getReference("users").child(userID)

            userref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(userData::class.java)
                        if (userProfile != null) {
                            binding.apply {
                                profileName.setText(userProfile.name.toString())
                                profileEmail.setText(userProfile.email.toString())
                                profileAddress.setText(userProfile.address.toString())
                                profilePhone.setText(userProfile.phone.toString())
                            }
                        }
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

}