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

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            navigateToLogin()
            return
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        val profileIcon: ImageView = findViewById(R.id.profile_icon)
        profileIcon.setOnClickListener {
            showProfileMenu(it)
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    @SuppressLint("ResourceType")
    private fun showProfileMenu(view: View) {
        val popupMenu = PopupMenu(this, view)

        val menuInflater = popupMenu.menuInflater
        menuInflater.inflate(R.layout.profile_menu, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        navigateToLogin()
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            navigateToLogin()
        }
    }
}
