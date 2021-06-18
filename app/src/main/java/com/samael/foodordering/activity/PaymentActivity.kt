package com.samael.foodordering.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.samael.foodordering.R

class PaymentActivity : AppCompatActivity() {

    lateinit var cardNumber: TextInputEditText
    lateinit var expiryDate: TextInputEditText
    lateinit var cvvNumber: TextInputEditText
    lateinit var proceedtopaybtn: Button
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        cardNumber = findViewById(R.id.cardNumber)
        expiryDate = findViewById(R.id.expiryDate)
        cvvNumber = findViewById(R.id.cvvNumber)
        proceedtopaybtn = findViewById(R.id.proceedtopaybtn)
        toolbar = findViewById(R.id.toolbarpayment)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Payment"


        proceedtopaybtn.setOnClickListener {
            if (cardNumber.text.toString() == "4111111111111111" && expiryDate.text.toString() == "05/23" && cvvNumber.text.toString() == "756") {
                val intent = Intent(this@PaymentActivity, PaymentControlActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@PaymentActivity, "Incorrect Details", Toast.LENGTH_LONG).show()
            }
        }

    }
}