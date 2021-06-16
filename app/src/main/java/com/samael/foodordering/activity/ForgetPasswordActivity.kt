package com.samael.foodordering.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.samael.foodordering.R
import com.samael.foodordering.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class ForgetPasswordActivity : AppCompatActivity() {

    lateinit var etMobile: EditText
    lateinit var etEmailAddress: EditText
    lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        etMobile = findViewById(R.id.etMobile)
        etEmailAddress = findViewById(R.id.etEmailAddress)
        btnNext = findViewById(R.id.btnNext)

        btnNext.setOnClickListener {

            val mobile_number = etMobile.text.toString()
            val email = etEmailAddress.text.toString()

            if (mobile_number != "" && email != "") {
                val queue = Volley.newRequestQueue(this@ForgetPasswordActivity)
                val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

                val jsonObject = JSONObject()

                jsonObject.put("mobile_number", mobile_number)
                jsonObject.put("email", email)

                if (ConnectionManager().checkConnectivity(this@ForgetPasswordActivity)) {
                    val jsonObjectRequest =
                        object : JsonObjectRequest(Method.POST, url, jsonObject, Response.Listener {

                            try {

                                val data = it.getJSONObject("data")

                                val success = data.getBoolean("success")

                                if (success) {
                                    val intent = Intent(
                                        this@ForgetPasswordActivity,
                                        ResetPasswordActivity::class.java
                                    )

                                    val bundle = Bundle()

                                    bundle.putString("mobile_number", mobile_number)

                                    intent.putExtra("bundle", bundle)

                                    if (!data.getBoolean("first_try")) {
                                        Toast.makeText(
                                            this@ForgetPasswordActivity,
                                            "Enter previously sent OTP",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    startActivity(intent)

                                    finish()

                                } else {
                                    Toast.makeText(
                                        this@ForgetPasswordActivity,
                                        "Enter Correct Details",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this@ForgetPasswordActivity,
                                    "Some Unexpected Error Occur",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        },
                            Response.ErrorListener {

                                Toast.makeText(
                                    this@ForgetPasswordActivity,
                                    "Volley error occurred!",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "c53fffb51ed23c"
                                return headers
                            }
                        }

                    queue.add(jsonObjectRequest)

                } else {
                    val dialog = AlertDialog.Builder(this@ForgetPasswordActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection Not Found")
                    dialog.setPositiveButton("open setting")
                    { text, listner ->
                        val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingIntent)
                        finish()
                    }
                    dialog.setNegativeButton("exit")
                    { text, listner ->
                        ActivityCompat.finishAffinity(this@ForgetPasswordActivity)
                    }

                    dialog.create()
                    dialog.show()
                }
            } else {
                Toast.makeText(this@ForgetPasswordActivity, "Enter Details", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }
    override fun onBackPressed() {
        val intent = Intent(this@ForgetPasswordActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
