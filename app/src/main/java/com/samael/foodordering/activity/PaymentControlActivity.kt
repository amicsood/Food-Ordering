package com.samael.foodordering.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.samael.foodordering.R

class PaymentControlActivity : AppCompatActivity() {

    lateinit var successbtn : Button
    lateinit var failurebtn : Button
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_control)

        successbtn = findViewById(R.id.successbtn)
        failurebtn = findViewById(R.id.failurebtn)

        toolbar=findViewById(R.id.toolbarcontrol)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Payment Result"

        successbtn.setOnClickListener {
            val intent= Intent(this@PaymentControlActivity,ConfirmationActivity::class.java)
            startActivity(intent)
            finish()
        }

        failurebtn.setOnClickListener {
            val intent= Intent(this@PaymentControlActivity,DeclinedOrderActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}