package com.samael.foodordering.activity

import android.content.Intent
import com.samael.foodordering.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.samael.foodordering.fragment.HomeFragment


class ConfirmationActivity : AppCompatActivity() {

    lateinit var btnDone:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        btnDone=findViewById(R.id.btnDone)

        btnDone.setOnClickListener {
            val intent= Intent(this@ConfirmationActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
