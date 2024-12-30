package com.example.lentera.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.lentera.R

class MQTTConnectFragment : Fragment() {

    private lateinit var connectButton: Button
    private lateinit var brokerEditText: EditText
    private lateinit var topicEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mqtt_connect, container, false)

        connectButton = view.findViewById(R.id.buttonConnect)
        brokerEditText = view.findViewById(R.id.editTextBroker)
        topicEditText = view.findViewById(R.id.editTextTopic)

        connectButton.setOnClickListener {
            val broker = brokerEditText.text.toString()
            val topic = topicEditText.text.toString()
            connectToMQTT(broker, topic)
        }

        return view
    }

    private fun connectToMQTT(broker: String, topic: String) {
        MQTTManager.connect(requireContext(), broker, topic) { receivedTopic, message ->
            Log.d("MQTTConnectFragment", "Message received on topic $receivedTopic: $message")
            // Handle the received message (e.g., update UI)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MQTTManager.disconnect(requireContext())
    }
}
