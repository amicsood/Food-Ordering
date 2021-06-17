package com.samael.foodordering.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.samael.foodordering.fragment.*
import com.samael.foodordering.R

class DashboardActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    var previousMenuItem: MenuItem? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtUserName:TextView
    lateinit var txtMobileNumber:TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        //to add data to header

        val convertView=LayoutInflater.from(this@DashboardActivity).inflate(R.layout.drawer_header,null)

        txtUserName=convertView.findViewById(R.id.txtUserName)

        txtMobileNumber=convertView.findViewById(R.id.txtMobileNumber)

        txtUserName.text="Hello "+sharedPreferences.getString("name",null)

        txtMobileNumber.text="+91- "+sharedPreferences.getString("mobile_number",null)

        navigationView.addHeaderView(convertView)

        setUpToolbar()

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragment()).commit()

        supportActionBar?.title = "Home"

        navigationView.setCheckedItem(R.id.home)

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@DashboardActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            it.isChecked = true
            it.isCheckable = true

            previousMenuItem = it

            when (it.itemId) {
                R.id.home -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, HomeFragment()).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Home"

                }

                R.id.profile -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment()).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Profile"

                }

                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FavouritesFragment()).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Favourites"
                }

                R.id.orderHistory-> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, OrderHistoryFragment()).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Order History"
                }

                R.id.customerSupport -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, CustomerSupportFragment()).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Customer Support"
                }

                R.id.faq -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FaqFragment()).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Frequent Asked Questions"
                }

                R.id.logout -> {
                    val builder = AlertDialog.Builder(this@DashboardActivity)
                    builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes") { _, _ ->
                            val intent = Intent(this@DashboardActivity, LoginActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this@DashboardActivity, "Logout Successfully", Toast.LENGTH_SHORT).show()
                            clearPreference()
                            finish()
                        }
                        .setNegativeButton("No") {_, _ ->
                            val intent = Intent(this@DashboardActivity, DashboardActivity::class.java)
                            startActivity(intent)
                        }
                        .create()
                        .show()
                }

            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar() {

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)
        when (frag) {
            !is HomeFragment -> {
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragment())
                    .commit()
                supportActionBar?.title = "Home"
                navigationView.setCheckedItem(R.id.home)
            }
            else -> super.onBackPressed()
        }
    }
    
    fun clearPreference()
    {
        sharedPreferences.edit().clear().apply()
    }
}