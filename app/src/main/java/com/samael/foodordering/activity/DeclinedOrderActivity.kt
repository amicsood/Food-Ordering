package com.samael.foodordering.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.samael.foodordering.R

class DeclinedOrderActivity : AppCompatActivity() {

    lateinit var retryBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_declined_order)

        retryBtn = findViewById(R.id.btnRetry)

        retryBtn.setOnClickListener {
            val intent= Intent(this@DeclinedOrderActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}