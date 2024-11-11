package com.example.excercise_fragment_navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.example.lentera.R


class WelcomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        val startButton = view.findViewById<Button>(R.id.start)
        startButton.setOnClickListener {
//Code that runs when the button is clicked
            view.findNavController()
                .navigate(R.id.action_WelcomeFragment_to_BluetoothFragment)
        }
        return view
    }
}