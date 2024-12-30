package com.example.lentera.homepage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.lentera.R
import com.google.firebase.firestore.FirebaseFirestore

class WelcomeFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        // Reference to the welcome message TextView
        val welcomeMessage = view.findViewById<TextView>(R.id.welcomeMessage)

        // Ambil UID dari SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("USER_ID", null)

        if (userId != null) {
            // Fetch username from Firestore
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("username") ?: "User"
                        welcomeMessage.text = "Welcome $username"
                    } else {
                        Toast.makeText(context, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "User ID not found", Toast.LENGTH_SHORT).show()
        }

        // Setup button to navigate to the next fragment
        val startButton = view.findViewById<Button>(R.id.start)
        startButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_welcomeFragment_to_bluetoothFragment)
        }

        return view
    }
}
