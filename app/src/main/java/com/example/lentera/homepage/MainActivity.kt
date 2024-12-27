package com.example.lentera.homepage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.lentera.R
import com.example.lentera.homepage.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if user is not logged in
        if (auth.currentUser == null) {
            navigateToLogin()
            return
        }

        // Set up the NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up the BottomNavigationView with the NavController
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Set up the Profile Icon Click Listener
        val profileIcon: ImageView = findViewById(R.id.profile_icon)
        profileIcon.setOnClickListener {
            showProfileMenu(it)  // Call the function to show the menu
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    @SuppressLint("ResourceType")
    private fun showProfileMenu(view: View) {
        // Create the PopupMenu
        val popupMenu = PopupMenu(this, view)

        // Inflate the menu XML
        val menuInflater = popupMenu.menuInflater
        menuInflater.inflate(R.layout.profile_menu, popupMenu.menu)  // This refers to the menu file in res/menu/profile_menu.xml

        // Show the PopupMenu
        popupMenu.show()

        // Handle menu item click
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    logout()  // Call logout function
                    true
                }
                else -> false
            }
        }
    }

    private fun logout() {
        // Firebase logout logic here
        FirebaseAuth.getInstance().signOut()
        navigateToLogin()  // Redirect to login screen after logout
    }

    override fun onStart() {
        super.onStart()

        // Recheck user login state
        if (auth.currentUser == null) {
            navigateToLogin()
        }
    }
}
