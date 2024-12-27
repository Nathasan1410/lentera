package com.example.lentera.homepage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lentera.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var progressBar: ProgressBar // Declare ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val usernameEditText: EditText = findViewById(R.id.et_username)
        val emailEditText: EditText = findViewById(R.id.et_email)
        val passwordEditText: EditText = findViewById(R.id.et_password)
        val registerButton: Button = findViewById(R.id.btn_register)

        progressBar = findViewById(R.id.progressBar) // Initialize ProgressBar

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                showLoading(true) // Show the ProgressBar

                // Daftarkan pengguna ke Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        showLoading(false) // Hide the ProgressBar after task is completed

                        if (task.isSuccessful) {
                            // Simpan data pengguna ke Firestore
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                val userData = hashMapOf(
                                    "username" to username,
                                    "email" to email,
                                    "password" to password
                                )

                                db.collection("users").document(userId)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        // Pindah ke MainActivity setelah berhasil registrasi
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method untuk mengontrol tampilan ProgressBar
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE // Show ProgressBar
        } else {
            progressBar.visibility = View.GONE // Hide ProgressBar
        }
    }
}
