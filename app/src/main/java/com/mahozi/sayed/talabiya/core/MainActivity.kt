package com.mahozi.sayed.talabiya.core

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.StorageH
import com.mahozi.sayed.talabiya.databinding.ActivityMainBinding
import com.mahozi.sayed.talabiya.order.view.main.OrderFragment
import com.mahozi.sayed.talabiya.order.view.orderitem.CreateSubOrderItemFragment
import com.mahozi.sayed.talabiya.person.PersonFragment
import com.mahozi.sayed.talabiya.resturant.view.main.RestaurantFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        drawerLayout = binding.drawerLayout
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        if (savedInstanceState != null) {
            return
        }

        setContentView(binding.root)

        val orderFragment = OrderFragment()
        orderFragment.arguments = intent.extras
        supportFragmentManager.beginTransaction().add(R.id.order_container, orderFragment).commit()
        onCreateDrawer()
    }

    private fun onCreateDrawer() {
        val myToolbar = findViewById<View>(R.id.toolbar_main) as Toolbar
        setSupportActionBar(myToolbar)



        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()


        navigationView = findViewById<View>(R.id.navigation_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(
            NavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.order_navigation_menu -> {
                    val orderFragment = OrderFragment()
                    orderFragment.arguments = intent.extras
                    supportFragmentManager.beginTransaction().add(R.id.order_container, orderFragment).commit()
                }
                R.id.restaurant_navigation_menu -> {
                    val restaurantFragment = RestaurantFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.order_container, restaurantFragment).commit()

                }
                R.id.person_navigation_menu -> {
                    val personFragment = PersonFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.order_container, personFragment).commit()
                }
                R.id.export_database -> StorageH().backUp(this@MainActivity)
                else -> return@OnNavigationItemSelectedListener true
            }
            true
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (drawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentByTag("CreateSubOrderItemFragment") as CreateSubOrderItemFragment?
        if (f != null && f.onBackPressed()) {

        } else {
            super.onBackPressed()
        }
    }
}