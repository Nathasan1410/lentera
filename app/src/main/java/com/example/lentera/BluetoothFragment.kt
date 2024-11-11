package com.example.lentera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class BluetoothFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bluetooth, container, false)
        val startButton = view.findViewById<Button>(R.id.start)
        startButton.setOnClickListener {
//Code that runs when the button is clicked
            view.findNavController()
                .navigate(R.id.action_bluetoothFragment_to_welcomeFragment)
        }
        return view
    }
}