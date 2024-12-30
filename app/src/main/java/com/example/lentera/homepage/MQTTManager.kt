package com.example.lentera.homepage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lentera.R
import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

object MQTTManager {

    private var mqttClient: MqttClient? = null
    private var lastBroker: String? = null
    private var lastTopic: String? = null

    fun connect(context: Context, broker: String, topic: String, onMessageReceived: (String, String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Ensure broker URI has a scheme
                val validatedBroker = if (broker.startsWith("tcp://") || broker.startsWith("ssl://")) {
                    broker
                } else {
                    "tcp://$broker"
                }

                if (mqttClient == null || !mqttClient!!.isConnected) {
                    val clientId = MqttClient.generateClientId()
                    mqttClient = MqttClient(validatedBroker, clientId, null)

                    val options = MqttConnectOptions().apply {
                        isAutomaticReconnect = true
                        isCleanSession = true
                        connectionTimeout = 10
                    }

                    mqttClient?.setCallback(object : MqttCallback {
                        override fun connectionLost(cause: Throwable?) {
                            Log.e("MQTTManager", "Connection lost: ${cause?.message}")
                            showToast(context, "Connection lost: ${cause?.message}")
                        }

                        override fun messageArrived(topic: String, message: MqttMessage) {
                            val payload = String(message.payload)
                            Log.d("MQTTManager", "Message arrived on topic $topic: $payload")
                            onMessageReceived(topic, payload)
                        }

                        override fun deliveryComplete(token: org.eclipse.paho.client.mqttv3.IMqttDeliveryToken?) {
                            Log.d("MQTTManager", "Delivery complete")
                        }
                    })

                    mqttClient?.connect(options)
                    lastBroker = validatedBroker
                    lastTopic = topic
                    withContext(Dispatchers.Main) {
                        showToast(context, "Connected to broker at $validatedBroker")
                    }
                }

                mqttClient?.subscribe(topic)
                withContext(Dispatchers.Main) {
                    showToast(context, "Subscribed to topic: $topic")
                }
                Log.d("MQTTManager", "Subscribed to topic: $topic")
            } catch (e: MqttException) {
                withContext(Dispatchers.Main) {
                    showToast(context, "Error connecting to broker: ${e.message}")
                }
                Log.e("MQTTManager", "Error connecting to broker: ${e.message}")
            }
        }
    }

    fun disconnect(context: Context) {
        try {
            mqttClient?.disconnect()
            mqttClient = null
            showToast(context, "Disconnected from broker")
            Log.d("MQTTManager", "Disconnected from broker")
        } catch (e: MqttException) {
            showToast(context, "Error disconnecting: ${e.message}")
            Log.e("MQTTManager", "Error disconnecting from broker: ${e.message}")
        }
    }

    fun publish(topic: String, payload: String) {
        try {
            mqttClient?.publish(topic, MqttMessage(payload.toByteArray()))
            Log.d("MQTTManager", "Message published to topic $topic: $payload")
        } catch (e: MqttException) {
            Log.e("MQTTManager", "Error publishing message: ${e.message}")
        }
    }

    fun getLastConnectionDetails(): Pair<String?, String?> {
        return Pair(lastBroker, lastTopic)
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}