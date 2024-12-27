package com.example.lentera.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.lentera.R

class BluetoothFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bluetooth, container, false)
        val startButton = view.findViewById<Button>(R.id.start)
        startButton.setOnClickListener {

            view.findNavController()
                .navigate(R.id.action_bluetoothFragment_to_connectedFragment)
        }
        return view
    }
}